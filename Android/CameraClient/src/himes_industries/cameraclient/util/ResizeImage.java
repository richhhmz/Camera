package himes_industries.cameraclient.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.omg.CORBA.portable.ApplicationException;

/**
 *
 * @author splabbity
 */
public class ResizeImage {
    public static ImageIcon resize(ImageIcon icon) {
        //Thanks to ww.coderanch.com/t/331731/GUI/java/Resize-ImageIcon
        Image img = icon.getImage();
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
//        g.drawImage(img, 0, 0, 400, 225, null);//keeping the 16:9 ratio
        g.drawImage(img, 0, 0, 600, 337, null);//keeping the 16:9 ratio
        return new ImageIcon(bi);
    }
    
    //from http://stackoverflow.com/questions/1228381/scale-an-image-which-is-stored-as-a-byte-in-java
    public static byte[] scale(byte[] fileData, int width, int height) {
    	ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] scaled;
    	try {
    		BufferedImage img = ImageIO.read(in);
    		if(height == 0) {
    			height = (width * img.getHeight())/ img.getWidth(); 
    		}
    		if(width == 0) {
    			width = (height * img.getWidth())/ img.getHeight();
    		}
    		Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    		BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

    		ImageIO.write(imageBuff, "jpg", buffer);

    		scaled = buffer.toByteArray();
    	} catch (IOException e) {
    		System.out.println("Scale problem: "+e);
                scaled = new byte[0];
    	}
        return scaled;
    }
}
