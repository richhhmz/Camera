package himes_industries.cameraclient.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

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
        g.drawImage(img, 0, 0, 400, 225, null);//keeping the 16:9 ratio
        return new ImageIcon(bi);
    }
}
