/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.cameraclient.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Rich
 */
public class Talk {
    private static final int port = 59900;
    
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
            
            reader = new InputStreamReader(socket.getInputStream());
            msg = new BufferedReader(reader).readLine();

            
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
        return msg;
    }
}
