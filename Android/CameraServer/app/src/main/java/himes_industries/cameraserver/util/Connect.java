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
    public static final Object syncSnap = new Object();
    public static final Object syncDownload = new Object();

    private final static String TAG = "Connect";
    private final int PORT = 59900;
    private ServerSocket server = null;
    private Socket socket = null;
    private CameraServerActivity activity = null;

    public Connect(CameraServerActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        try {
            server = new ServerSocket(PORT);//Enable in AndroidManifest.xml!
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    protected Void doInBackground(Void... args) {
        InputStreamReader reader = null;
        try {
            while(true) {
                try {
                    socket = server.accept();
                    reader = new InputStreamReader(socket.getInputStream());
                    String request = new BufferedReader(reader).readLine().trim().toLowerCase();

                    if (request.equals(CameraControl.END)){
                        break;
                    }
                    else{
                        activity.getCameraControl().processRequest(socket, request);
                    }
                }
                finally {
                    if(reader != null) reader.close();
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        finally{
            try{
                socket.close();
            }
            catch(Exception ex){
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        try {
            if (server != null)
                server.close();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }
}