package main;

public class RouterTableRow {
	private int _destinationAddress;
	private String _linkName;
	private int _cost;
	
	public RouterTableRow(int destinationAddress, String linkName, int cost){
		_destinationAddress = destinationAddress;
		_linkName = linkName;
		_cost = cost;
	}
	
	public int getDestinationAddress(){
		return this._destinationAddress;
	}
	
	public String getLinkName(){
		return this._linkName;
	}
	
	public int getCost(){
		return this._cost;
	}
	
	public void setDestinationAddress(int destinationAddress){
		this._destinationAddress = destinationAddress;
	}
	
	public void setLinkName(String linkName){
		this._linkName = linkName;
	}
	
	public void setCost(int cost){
		this._cost = cost;
	}
}
