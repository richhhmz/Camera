package himes_industries.arduino;

import java.io.PrintStream;

import console.Dialog;

public class ArduinoGlobal {

//	public static int BAUD_RATE = 300;		// ~780  actual
//	public static int BAUD_RATE = 1200;		// ~1200 actual
//	public static int BAUD_RATE = 2400;		// ~2400 actual
//	public static int BAUD_RATE = 4800;		// ~4800 actual
	public static int BAUD_RATE = 9600;		// ~9k   actual
//	public static int BAUD_RATE = 19200;	// ~18k  actual
//	public static int BAUD_RATE = 38400;	// ~32k  actual
//	public static int BAUD_RATE = 57600;	// ~40k  actual
//	public static int BAUD_RATE = 115200;	// ~40k  actual

	public static String NEWLINE = System.getProperty("line.separator");

	public static void handleError(String message){
		System.out.printf("Error %s\n", message);
	}
	
	public static void handleException(Exception ex){
		System.out.printf("Caught exception: %s\n", ex.toString());
		ex.printStackTrace(new PrintStream(System.out));
	}
}
