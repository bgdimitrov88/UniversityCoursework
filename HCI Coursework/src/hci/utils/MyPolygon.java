package hci.utils;

import java.util.ArrayList;

public class MyPolygon {
	
	private ArrayList<MyPoint> polygonPoints;
	private String name;
	private String imageName;
	
	public MyPolygon(){
		this.polygonPoints = new ArrayList<MyPoint>();
		this.name = "";
		this.imageName = "";
	}
	
	public MyPolygon(ArrayList<MyPoint> polygonPoints){
		this.polygonPoints = polygonPoints;
		this.name = "";
		this.imageName = "";
	}
	
	public MyPolygon(ArrayList<MyPoint> polygonPoints, String name, String imageName){
		this.polygonPoints = polygonPoints;
		this.name = name;
		this.imageName = imageName;
	}
	
	public ArrayList<MyPoint> getPoints(){
		return this.polygonPoints;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name) {
		if(name != null && name != ""){
			this.name = name;
		}
	}
	
	public String getImageName(){
		return this.imageName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		MyPolygon cast = (MyPolygon) obj;
		
		if(cast == null)
			return false;
		
		if(
				cast.imageName.equals(this.imageName) 
				&& 
				cast.polygonPoints.equals(this.polygonPoints) 
				&& 
				cast.name.equals(this.name))
			return true;
		
		return false;
	}

}
