/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.cameraclient.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFileChooser;
import himes_industries.cameraclient.CameraClientFrame;
import javax.swing.ImageIcon;

/**
 * @author Rich
 */
public class Talk {
    
    public static final String SNAP =  "snap";
    
    public static final Object sync = new Object();
    private static final int port = 59900;
    private static byte[] buffer;
    public static boolean running = false;
    public static CameraClientFrame frame;
    private static boolean switcher = true;
    
    public static String sendMessage(String input) {
        Process process = null;
        BufferedOutputStream bos = null;
        InputStreamReader reader = null;
        Socket socket = null;
        String msg = "failed";
        try {
            System.out.println("Begin");
            Runtime runtime = Runtime.getRuntime();

            if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
                process = runtime.exec(
                    "C:/Users/Rich/AppData/Local/Android/sdk/platform-tools/adb forward tcp:59900 tcp:59900/"
                );
            }
            else{
                process = runtime.exec(
                    "/Users/splabbity/Desktop/adt-bundle-mac-x86_64-20131030/sdk/platform-tools/adb forward tcp:59900 tcp:59900"
                );
            }
            
            Scanner sc = new Scanner(process.getErrorStream());
            while(sc.hasNext()){
                System.err.print(sc.next());
            }
            
            //Starting socket
            socket = new Socket("127.0.0.1", port);

            bos = new BufferedOutputStream(socket.getOutputStream());
			
            //Sending text typed above to app
            input = input + "\r\n";
            bos.write(input.getBytes());
            bos.flush();
            
            if(input.toLowerCase().startsWith(SNAP)){
                receive(socket);
                msg = "snapped";
            }
            else{
                reader = new InputStreamReader(socket.getInputStream());
                msg = new BufferedReader(reader).readLine();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println();
        }
        finally{
            try{
                if(bos != null) bos.close();
            }
            catch(Exception dontCare){}
            try{
                if(reader != null) reader.close();
            }
            catch(Exception dontCare){}
        }
            
        synchronized(sync){
            sync.notify();
        }
        return msg;
    }
    
    private static void receive(Socket socket) throws Exception {
        ObjectInputStream ois;
        while(true){
            try{
                ois = new ObjectInputStream(socket.getInputStream());
                if(ois == null) continue;
                break;
            }
            catch(Exception ex){
                //Thread.sleep(100);
            }
        }
        buffer = (byte[])ois.readObject();
        switcher = !switcher;
    }
    
        public static void start() {
            running = true;
            Thread t =  new Thread(new Continuous());
            t.start();
        }
        
        public static void stop(){
            running = false;
        }
    
    public static void autoSave() throws Exception {
        String filename;
        if (switcher)
            filename = "image_a.b64";
        else
            filename = "image_b.b64";
                
        if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
            //overwrite previous file with same name.
            FileOutputStream fos = new FileOutputStream("C:/Users/Rich/Documents/GitHub/Camera/node.js/RealTimeWebChat/"+filename, false);
            fos.write(Base64Enc.encode(ResizeImage.scale(buffer, 900, 450)));//use buffer64 for Base64 version
        }
        else{
            //FileOutputStream fos = new FileOutputStream("/Users/splabbity/NetBeansProjects/CameraClient/src"+filename);
            FileOutputStream fos = new FileOutputStream("/Users/splabbity/NetBeansProjects/node.js/RealtimeWebChat/"+filename, false);//
            fos.write(Base64Enc.encode(ResizeImage.scale(buffer, 900, 450)));
            
        }
    }
    

    public static void saveFile() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showSaveDialog(new himes_industries.cameraclient.CameraClientFrame());
        if(choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(buffer); //save jpg file
                //fos.write(Base64Enc.encode(ResizeImage.scale(buffer, 900, 450))); //save "b64" file
                
                fos.flush();
                fos.close();
            }

            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public static void openFile() throws Exception {
        try{
            JFileChooser fileChooser = new JFileChooser();
            int choice = fileChooser.showOpenDialog(new himes_industries.cameraclient.CameraClientFrame());
            if(choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //BufferedInputStream in = (BufferedInputStream)himes_industries.cameraclient.CameraClientFrame.class.getResourceAsStream(file.toString());
                FileInputStream fin = new FileInputStream(file);
                buffer = new byte[fin.available()];
                fin.read(buffer);
                fin.close();
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public static byte[] getBuffer() {
        return buffer;
    }
    
    public static byte[] getDecodedBuffer() {
        return Base64Enc.decode(buffer);
    }
}
