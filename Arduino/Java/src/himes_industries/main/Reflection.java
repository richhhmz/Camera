package himes_industries.main;

import himes_industries.util.Communication;
import himes_industries.util.Global;
import himes_industries.util.Message;

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
		String port = args[0];
		Communication.connect(port);
		
		int c = -1;
		System.out.println("Type a character at a time and enter.");
		System.out.println("When finished, type a \".\"");
		while(true){
			c = System.in.read();
			while(c == '\r' || c == '\n'){
				c = System.in.read();
			}
			if(c == '.') break;
			
			Communication.sendInt(c);
			Message.sent(c);
		}
		//Mac: If this program runs more than once without unplugging/replugging the USB, it freezes.
		Communication.closeCommPort();
		System.out.println("end");
		
	}

}
