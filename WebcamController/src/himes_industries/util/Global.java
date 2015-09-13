/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.util;

import java.io.PrintStream;

/**
 *
 * @author Rich
 */
public class Global {
	
	public static void handleException(Exception ex){
                String msg = ex.getMessage() == null? "":ex.getMessage();
		System.out.printf("Caught exception: %s\n", msg);
		ex.printStackTrace(new PrintStream(System.out));
	}
    
}
