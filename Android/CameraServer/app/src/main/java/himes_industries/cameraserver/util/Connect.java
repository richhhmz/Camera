package himes_industries.cameraserver.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import himes_industries.cameraserver.CameraServerActivity;

/**
 * Created by splabbity on 2/28/15.
 */
public class Connect extends AsyncTask<Void, Void, Void> {
    private final static String TAG = "Connect";
    private final int PORT = 59900;
    private ServerSocket server = null;
    private Socket socket = null;
    CameraServerActivity activity = null;
    private ObjectOutputStream oosImage;
    public static final Object syncSnap = new Object();
    public static final Object syncDownload = new Object();

    public Connect(CameraServerActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        try {
            server = new ServerSocket(PORT);//Enable in AndroidManifest.xml!
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected Void doInBackground(Void... args) {
        String msg = "";
        String response = "";
        boolean end = false;
        InputStreamReader reader = null;
        OutputStreamWriter writer = null;

        try {
            while(!end) {
                try {
                    socket = server.accept();
                    reader = new InputStreamReader(socket.getInputStream());
                    msg = new BufferedReader(reader).readLine();
                    if(msg.equalsIgnoreCase(CameraControl.SNAP)){

                        synchronized (syncSnap) {
                            activity.capture();
                            syncSnap.wait(15000);
                        }

                        synchronized (syncDownload) {
                            oosImage = new ObjectOutputStream(socket.getOutputStream());
                            oosImage.writeObject(activity.getImage());
                            oosImage.flush();
                            syncDownload.notify();
                        }
                    }
                    else {
                        response = CameraControl.processRequest(msg);

                        writer = new OutputStreamWriter(socket.getOutputStream());
                        writer.write(String.format("Response=\"%s\"\r\n", response));
                        writer.flush();
                        if (msg.equalsIgnoreCase(CameraControl.END)) end = true;
                    }
                }
                finally {
                    if(reader != null) reader.close();
                    if(writer != null) writer.close();
                    if(oosImage != null) oosImage.close();
                }
            }

        } catch (Exception e) {
            msg = String.format("Caught Exception \"%s\"\r\n", e.getMessage());
            Log.e(TAG, msg, e);
        }
        finally{
            try{
                socket.close();
            }
            catch(Exception dontCare){}
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        try {
            if (server != null)
                server.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}