package hci.utils;

import java.awt.image.BufferedImage;

public class MyImage {
	
	private BufferedImage image;
	private String name;
	
	public MyImage(BufferedImage image, String name){
		this.image = image;
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
