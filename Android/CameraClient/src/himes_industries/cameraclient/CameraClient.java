package himes_industries.cameraclient;

import java.util.GregorianCalendar;
import javax.swing.JFrame;

/**
 * @author Rich
 */
public class CameraClient {

    public static void main(String[] args) {
        GregorianCalendar gc = new GregorianCalendar();
        
        CameraClientFrame frame = new CameraClientFrame();
        frame.setTitle("Camera Client");
        frame.setSize(648, 435);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
