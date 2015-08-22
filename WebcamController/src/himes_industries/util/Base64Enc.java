/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package himes_industries.util;

import java.util.Base64;

/**
 *
 * @author splabbity
 */
public class Base64Enc {
    public static byte[] encode (byte[] buffer) {
        return Base64.getEncoder().encode(buffer);
    }
    
    public static byte[] decode (byte[] buffer) {
        return Base64.getDecoder().decode(buffer);
    }
}
