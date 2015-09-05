/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.android;

import himes_industries.util.ResizeImage;
import himes_industries.util.Base64Enc;
import himes_industries.util.Post;
import himes_industries.webcam.WebcamControllerJFrame;
import javax.swing.ImageIcon;

/**
 *
 * @author Rich
 */
public class ContinuousPics implements Runnable {
    private WebcamControllerJFrame frame;
    
    public ContinuousPics(WebcamControllerJFrame frame){
        this.frame =  frame;
    }
    
    @Override
    public void run(){
        
        while(AndroidComm.running){
            AndroidComm.sendMessage(AndroidComm.SNAP);
            ImageIcon icon = new ImageIcon(AndroidComm.getBuffer());
            AndroidComm.frame.getLblPicture().setIcon(ResizeImage.resize(icon));
            
            try {
                new Post(frame, Base64Enc.encode(ResizeImage.scale(AndroidComm.getBuffer(), 900, 450))).run();
            }
            catch (Exception e) {
                System.out.println(e + ":(");
            }
            
        }
    }
}
