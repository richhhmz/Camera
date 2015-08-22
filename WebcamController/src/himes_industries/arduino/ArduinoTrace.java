package himes_industries.arduino;

public class ArduinoTrace {

	public static void sent(int c){
		System.out.printf("Sent message: %s\n", Character.toString((char)c));
	}

	public static void sent(String message){
		System.out.printf("Sent message: %s\n", message);
	}
		
	public static void received(String message){
		System.out.printf("Received message: %s\n", message);
	}

	public static void info(String message){
		System.out.printf("Info: %s\n", message);
	}
}
