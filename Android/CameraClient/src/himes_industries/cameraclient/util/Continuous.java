/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.cameraclient.util;

import javax.swing.ImageIcon;

/**
 *
 * @author Rich
 */
public class Continuous implements Runnable {
    
    
    public void run(){
        
        while(Talk.running){
            Talk.sendMessage(Talk.SNAP);
            ImageIcon icon = new ImageIcon(Talk.getBuffer());
            Talk.frame.getLblPicture().setIcon(ResizeImage.resize(icon));
            
            try {
                //Talk.autoSave();
                new Post(Base64Enc.encode(ResizeImage.scale(Talk.getBuffer(), 900, 450))).run();
            }
            catch (Exception e) {
                System.out.println(e + ":(");
                
            }
            
        }
    }
}
