package hci.utils;

import java.util.ArrayList;

public class Polygon {
	
	private ArrayList<Point> polygonPoints;
	private String name;
	
	public Polygon(ArrayList<Point> polygonPoints, String name){
		this.polygonPoints = polygonPoints;
		this.name = name;
	}
	
	public ArrayList<Point> getPoints(){
		return this.polygonPoints;
	}
	
	public String getName(){
		return this.name;
	}

}
