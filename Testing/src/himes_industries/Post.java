package himes_industries;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class Post {

	public static void main(String[] args) {
		try{
			System.out.println("Start");
			Post post = new Post();
			post.run(args);
		}
		catch(Exception ex){
			System.out.printf("Caught exception: %s\n", ex.toString());
			ex.printStackTrace(new PrintStream(System.out));
		}
		System.out.println("End");
	}
	
	private void run(String[] args) throws Exception{
		// See
		// http://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
		// http://hc.apache.org/httpcomponents-core-4.4.x/httpcore/examples/org/apache/http/examples/ElementalHttpPost.java
		// https://codeforgeek.com/2014/09/handle-get-post-request-express-4/
		// Requires Apache httpcore, httpclient, and commons-io libraries (see lib folder).
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://localhost:3700");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("image", "This message will be replaced by a base 64 image."));
		httppost.setEntity(new UrlEncodedFormEntity(params));

		//Execute and get the response.
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity responseData = response.getEntity();

		if (responseData != null) {
		    InputStream instream = responseData.getContent();
		    try {
		    	byte[] bytes = IOUtils.toByteArray(instream);
		        System.out.println(String.format("response=%s\n", new String(bytes)));
		    } finally {
		        instream.close();
		    }
		}
	}
}
