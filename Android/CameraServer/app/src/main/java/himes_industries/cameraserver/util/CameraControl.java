package himes_industries.cameraserver.util;

import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.util.List;

import himes_industries.cameraserver.CameraServerActivity;

/**
 * Created by Rich on 5/2/2015.
 */
public class CameraControl {

    private final static String TAG = "CameraControl";
    public final static String END = "end";
    public final static String SNAP = "snap";
    public final static String ZOOM = "zoom ";
    public final static String APERTURE = "aperture ";
    public final static String SHUTTER = "shutter ";

    CameraServerActivity activity;

    public CameraControl(CameraServerActivity activity){
        this.activity = activity;
    }

    public void processRequest(Socket socket, String request){
        String response = null;
        request = request.toLowerCase();
        try {
            if (request.equals(SNAP)) {
                doSnap(socket);
                response = null; // We are returning an image instead of a textual response.
            } else if (request.startsWith(ZOOM)) {
                doZoom(socket, request);
                response = request.replace(ZOOM, "Zoomed ");
            } else if (request.startsWith(APERTURE)) {
                response = request.replace(APERTURE, "Set aperture to ");
            } else if (request.startsWith(SHUTTER)) {
                response = request.replace(SHUTTER, "Set shutter speed to ");
            } else if (request.equals(END)) {
                response = "Ciao!";
            }
            else{
                response = String.format("Invalid request: %s", request);
            }
        }
        catch(Exception ex){
            response = String.format("Caught exception: %s", ex.getMessage());
            Log.e(TAG, ex.getMessage(), ex);
        }
        if(response != null){
            try {
                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write(String.format("Response=\"%s\"\r\n", response));
                writer.flush();
            }
            catch(Exception ex){
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    }

    private void doSnap(Socket socket){
        try {
            synchronized (Connect.syncSnap) {
                activity.capture();
                Connect.syncSnap.wait(15000);
            }

            synchronized (Connect.syncDownload) {
                ObjectOutputStream oosImage = new ObjectOutputStream(socket.getOutputStream());
                oosImage.writeObject(activity.getImage());
                oosImage.flush();
                oosImage.close();
                Connect.syncDownload.notify();
            }
        }
        catch(Exception ex){
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private void doZoom(Socket socket, String request){
        String split[] = request.split(" ");
        int zoom = Integer.parseInt(split[1]);
        Camera camera = activity.getCamera();
        Camera.Parameters params = camera.getParameters();
//        List<Integer> ratios = params.getZoomRatios();
//        int max = params.getMaxZoom();
        params.setZoom(zoom);
        camera.setParameters(params);
    }
}
