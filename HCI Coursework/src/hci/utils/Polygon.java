package hci.utils;

import java.util.ArrayList;

public class Polygon {
	
	private ArrayList<Point> polygonPoints;
	private String name;
	private String imageName;
	
	public Polygon(){
		this.polygonPoints = new ArrayList<Point>();
		this.name = "";
		this.imageName = "";
	}
	
	public Polygon(ArrayList<Point> polygonPoints, String name, String imageName){
		this.polygonPoints = polygonPoints;
		this.name = name;
		this.imageName = imageName;
	}
	
	public ArrayList<Point> getPoints(){
		return this.polygonPoints;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getImageName(){
		return this.imageName;
	}

}
