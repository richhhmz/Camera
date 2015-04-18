package himes_industries.main;

import himes_industries.util.Communication;
import himes_industries.util.Global;
import himes_industries.util.Message;
import javax.swing.JFrame;

//Mac version: will work if librxtxSerial.jnilib is copied to lib folder.
//http://blog.iharder.net/2009/08/18/rxtx-java-6-and-librxtxserial-jnilib-on-intel-mac-os-x/

public class Reflection {

	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Usage: java himes_industries.main.Reflection <port>");
			System.exit(-1);
		}
		try{
			run(args);
		}
		catch(Exception ex){
			Global.handleException(ex);
		}
	}
	
	private static void run(String[] args) throws Exception{
		SliderGUI frame = new SliderGUI(args);
		frame.setSize(800,800);
		frame.setTitle("Servo control");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Communication.closeCommPort();		
	}
}
