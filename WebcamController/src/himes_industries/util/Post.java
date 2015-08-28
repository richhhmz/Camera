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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


public class Post {
    
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
                        .key("image")
                        .value(new String(buffer))
                        .key("timestamp")
                        .value(new java.util.Date().toString())
                        .key("settings")
                        .object()
                            .key("pan")
                            .value(frame.getPan())
                            .key("tilt")
                            .value(frame.getTilt())
                            .key("zoom")
                            .value(frame.getZoom())
                        .endObject()
                        .key("ranges")
                        .object()
                            .key("panMin")
                            .value(frame.getPanMin())
                            .key("panMax")
                            .value(frame.getPanMax())
                            .key("tiltMin")
                            .value(frame.getTiltMin())
                            .key("tiltMax")
                            .value(frame.getTiltMax())
                            .key("zoomMin")
                            .value(frame.getZoomMin())
                            .key("zoomMax")
                            .value(frame.getZoomMax())
                        .endObject()
                    .endObject()
                .endObject()
            .toString();
        json = new JSONObject(message);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("message", message));
        
        //Execute and get the response.
        httppost.setEntity(new UrlEncodedFormEntity(params));
        
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity responseData = response.getEntity();
        
        if (responseData != null) {
            InputStream instream = responseData.getContent();
            try {
                byte[] bytes = IOUtils.toByteArray(instream);
                System.out.println(String.format("response=%s", new String(bytes)));

                JSONObject jsonObject = new JSONObject(new String(bytes));
                String status = jsonObject.getJSONObject("response").getString("status");
                System.out.println(String.format("status=%s", status));
                JSONObject settings = jsonObject.getJSONObject("response").getJSONObject("settings");
                System.out.println(String.format("settings: pan=%s, tilt=%s, zoom=%s",
                        settings.getString("pan"), settings.getString("tilt"), settings.getString("zoom")));
                frame.setPan(settings.getString("pan"));
                frame.setTilt(settings.getString("tilt"));
                frame.setZoom(settings.getString("zoom"));
                frame.setPanTiltZoom();
            } finally {
                instream.close();
            }
        }
    }
}
