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
import java.util.Base64;
import java.util.GregorianCalendar;

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
    private static String filename;
    
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
                Thread.sleep(100);
            }
        }
        buffer = (byte[])ois.readObject();
        
        //began experimenting after this point
        
        Base64.Encoder enc = Base64.getEncoder();
        byte[] buffer64 = enc.encode(buffer);//only byte[] allowed.
        
        //Base64 test.
        String hw = "Hello World!";
        byte[] hwB = hw.getBytes();
        
        System.out.println("Good Old Fashioned Hello World: "+hw);
        System.out.println("Hello World in byte[] form: "+hwB);
        
        byte[] encHwB = enc.encode(hwB);
        String encHw = new String(encHwB);
        
        System.out.println("Byte[] form in Base64: "+encHwB);
        System.out.println("Base64 byte[] to String: "+encHw); //Displays "SGVsbG8gV29ybGQh" like it should.
        
        System.out.println("Base64 byte[] decoded and changed back to String: "+new String(Base64.getDecoder().decode(encHwB)));
        
        //System.out.println(enc.encode("Hello World!".getBytes()));
        
        GregorianCalendar gc = new GregorianCalendar();
        //filename = "/image/"+gc.getTimeInMillis()+".jpg";
        filename = "IMG"+gc.getTimeInMillis()+".jpg";
                
        if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
            FileOutputStream fos = new FileOutputStream("***WHEREVER YOUR /PUBLIC IS***"+filename);
            fos.write(buffer);//use buffer64 for Base64 version
        }
        else{
            //FileOutputStream fos = new FileOutputStream("/Users/splabbity/NetBeansProjects/CameraClient/src"+filename);
            FileOutputStream fos = new FileOutputStream("/Users/splabbity/json/public/"+filename);//
            fos.write(buffer);
        }
    }
    
        public static void start() {
            running = true;
            Thread t =  new Thread(new Continuous());
            t.start();
        }
        
        public static void stop(){
            running = false;
        }

    public static void saveFile() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showSaveDialog(new himes_industries.cameraclient.CameraClientFrame());
        if(choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(buffer);
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
}
