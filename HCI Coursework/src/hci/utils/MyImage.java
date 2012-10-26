package hci.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class MyImage {
	
	private BufferedImage image;
	private String name;
	
	public MyImage(BufferedImage image, String name){
		
		this.image = new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB);
		this.image.createGraphics().drawImage(image.getScaledInstance(800, 600, Image.SCALE_SMOOTH), 0, 0, null);
		this.name = name;
	}
	
	public BufferedImage getOriginalImage(){
		return HelperMethods.copyImage(this.image);
	}
	
	public BufferedImage getImage(){
		return this.image;
	}
	
	public String getName(){
		return this.name;
	}
}
