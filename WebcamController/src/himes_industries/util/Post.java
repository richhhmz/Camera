package himes_industries.util;

import himes_industries.webcam.WebcamControllerJFrame;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Post {
    
    private static boolean clear = false;
    private static boolean fix = false;
    private static String lastChanged = "";

    private WebcamControllerJFrame frame;
    private byte[] buffer;
    
    public Post(WebcamControllerJFrame frame, byte[] buffer) {
        this.frame = frame;
        this.buffer = buffer;
    }
    
    public void run() throws Exception{
        // See
        // http://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
        // http://hc.apache.org/httpcomponents-core-4.4.x/httpcore/examples/org/apache/http/examples/ElementalHttpPost.java
        // https://codeforgeek.com/2014/09/handle-get-post-request-express-4/
        // Requires Apache httpcore, httpclient, and commons-io libraries (see lib folder).
        
        JSONObject json;
        
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(frame.getWebServerUrl());

        String message = new JSONStringer()
                .object()
                    .key("message")
                    .object()
                        .key("timestamp")
                        .value(new java.util.Date().toString())
                        .key("lastChanged")
                        .value(lastChanged)
                        .key("lastChangedBy")
                        .value(frame.getLastChangedBy())
                        .key("controls")
                        .object()
                            .key("clear")
                            .value(clear?"true":"false")
                            .key("fix")
                            .value(fix?"true":"false")
                        .endObject()
                        .key("settings")
                        .object()
                            .key("pan")
                            .value(frame.getPan())
                            .key("tilt")
                            .value(frame.getTilt())
                            .key("zoom")
                            .value(frame.getZoom())
                        .endObject()
                        .key("zoomTable")
                        .array()
                            .value("1.0")
                            .value("1.2")
                            .value("1.5")
                            .value("1.8")
                            .value("2.2")
                            .value("2.8")
                            .value("3.4")
                            .value("4.0")
                            .value("5.0")
                            .value("6.1")
                            .value("7.5")
                            .value("9.4")
                            .value("11.4")
                            .value("13.9")
                            .value("17.9")
                            .value("21.0")
                        .endArray()
                        .key("image")
                        .value(new String(buffer))
                    .endObject()
                .endObject()
            .toString();
        json = new JSONObject(message);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("message", message));
        
        //Execute and get the response.
        httppost.setEntity(new UrlEncodedFormEntity(params));
        
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity responseData = response.getEntity();
        
        if (responseData != null) {
            InputStream instream = responseData.getContent();
            try {
                byte[] bytes = IOUtils.toByteArray(instream);
//                System.out.println(String.format("response=%s", new String(bytes)));

                JSONObject jsonObject = new JSONObject(new String(bytes));
                String status = jsonObject.getJSONObject("response").getString("status");
                String lastChangedBy = jsonObject.getJSONObject("response").getString("lastChangedBy");
//                System.out.println(String.format("status=%s, name=%s", status, name));
                JSONObject settings = jsonObject.getJSONObject("response").getJSONObject("settings");
                verifyChanges(settings);
                if(! lastChangedBy.equals("")){
                    frame.setLastChangedBy(lastChangedBy);
                }
//                System.out.println(String.format("settings: pan=%s, tilt=%s, zoom=%s",
//                        settings.getString("pan"), settings.getString("tilt"), settings.getString("zoom")));
                
            } finally {
                instream.close();
            }
        }
    }
    
    private void verifyChanges(JSONObject settings){

        boolean changed;
        
        int pan = Integer.parseInt(settings.getString("pan"));
        int tilt = Integer.parseInt(settings.getString("tilt"));
        int zoom = Integer.parseInt(settings.getString("zoom"));
        int framePan = Integer.parseInt(frame.getPan());
        int frameTilt = Integer.parseInt(frame.getTilt());
        int frameZoom = Integer.parseInt(frame.getZoom());

        if(pan == framePan && tilt == frameTilt && zoom == frameZoom){
            changed = false;
        }
        else{
//            System.out.println(String.format("changed from %d.%d.%d to %d.%d.%d",
//                    framePan, frameTilt, frameZoom, pan, tilt, zoom));
            lastChanged = new java.util.Date().toString();
            changed = true;
        }

        int panMin = Integer.parseInt(frame.getPanMin());
        int panMax = Integer.parseInt(frame.getPanMax());
        int tiltMin = Integer.parseInt(frame.getTiltMin());
        int tiltMax = Integer.parseInt(frame.getTiltMax());
        int zoomMin = Integer.parseInt(frame.getZoomMin());
        int zoomMax = Integer.parseInt(frame.getZoomMax());
        
        if(pan < panMin || pan > panMax || tilt < tiltMin || tilt > tiltMax || zoom < zoomMin || zoom > zoomMax){
            fix = true;
        }
        else{
            fix = false;
        }
        
        if(pan < panMin) pan = panMin;
        if(pan > panMax) pan = panMax;
        if(tilt < tiltMin) tilt = tiltMin;
        if(tilt > tiltMax) tilt = tiltMax;
        if(zoom < zoomMin) zoom = zoomMin;
        if(zoom > zoomMax) zoom = zoomMax;

        frame.setPan("" + pan);
        frame.setTilt("" + tilt);
        frame.setZoom("" + zoom);
        frame.setPanTiltZoom();
        
        clear = changed;
    }
}
