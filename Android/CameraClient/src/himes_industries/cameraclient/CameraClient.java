package himes_industries.cameraclient;

import javax.swing.JFrame;

/**
 * @author Rich
 */
public class CameraClient {

    public static void main(String[] args) {
        CameraClientFrame frame = new CameraClientFrame();
        frame.setTitle("Camera Client");
        frame.setSize(490, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
