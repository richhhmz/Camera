/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.webcam;

import javax.swing.JFrame;

/**
 *
 * @author Rich
 */
public class WebcamController {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	WebcamControllerJFrame frame = new WebcamControllerJFrame();
        frame.setSize(800,800);
        frame.setTitle("Webcam control");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
