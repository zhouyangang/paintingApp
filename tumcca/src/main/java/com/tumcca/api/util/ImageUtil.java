package com.tumcca.api.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-03-23
 */
public class ImageUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);
	
	
    public static BufferedImage createThumbnail(BufferedImage img) {
        return resizeImage(img, 150, 100);
    }

    public static BufferedImage resizeImage(BufferedImage img, int targetWidth, int targetHeight) {
        return Scalr.resize(img, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
                targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
    
    public static Integer[] getDimensions(File fileImg) throws Exception{
    	BufferedImage bufferedImage = ImageIO.read(fileImg);
	    return new Integer[]{bufferedImage.getWidth(), bufferedImage.getHeight()};
    }
    
}
