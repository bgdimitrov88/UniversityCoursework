package hci.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class HelperMethods {
	public static BufferedImage copyImage(BufferedImage img){
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
