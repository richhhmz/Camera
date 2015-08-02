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
            byte[] bytes = Talk.getBuffer();
            ImageIcon icon = new ImageIcon(bytes);
            Talk.frame.getLblPicture().setIcon(ResizeImage.resize(icon));
            
            try {
                Talk.autoSave();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            
        }
    }
}
