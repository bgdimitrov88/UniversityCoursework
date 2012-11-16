package main;

public class NetworkNode {
	private String _name;
	private int[] _addresses;
	
	public NetworkNode(String name, int[] addresses){
		_name = name;
		_addresses = addresses;
	}
	
	public String getName(){
		return this._name;
	}
	
	public int[] getAddresses(){
		return this._addresses;
	}

}
