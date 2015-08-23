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
                
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(frame.getWebServerUrl());
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", new String(buffer)));
        params.add(new BasicNameValuePair("pan", frame.getPan()));
        params.add(new BasicNameValuePair("tilt", frame.getTilt()));
        params.add(new BasicNameValuePair("zoom", frame.getZoom()));
        params.add(new BasicNameValuePair("timestamp", new java.util.Date().toString()));
        
        //Execute and get the response.
        httppost.setEntity(new UrlEncodedFormEntity(params));
        
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity responseData = response.getEntity();
        
        if (responseData != null) {
            InputStream instream = responseData.getContent();
            try {
                byte[] bytes = IOUtils.toByteArray(instream);
                System.out.println(String.format("response=%s", new String(bytes)));

                JSONObject json = new JSONObject(new String(bytes));
                String status = json.getJSONObject("response").getString("status");
                System.out.println(String.format("status=%s", status));
                JSONObject settings = json.getJSONObject("response").getJSONObject("settings");
                System.out.println(String.format("settings: pan=%s, tilt=%s, zoom=%s",
                        settings.getString("pan"), settings.getString("tilt"), settings.getString("zoom")));
            } finally {
                instream.close();
            }
        }
    }
}
