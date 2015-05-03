package himes_industries.cameraserver.util;

import android.util.Log;

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

    public static String processRequest(String request){
        String response;
        request = request.toLowerCase();
        try {
            if (request.equals(SNAP)) {
                response = "Snap!";
            } else if (request.startsWith(ZOOM)) {
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
        return response;
    }
}
