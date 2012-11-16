package main;

public class NetworkLink {
	private String _leftEnd;
	private String _rightEnd;
	
	public NetworkLink(String leftEnd, String rightEnd){
		_leftEnd = leftEnd;
		_rightEnd = rightEnd;
	}
	
	public String getLeftEnd(){
		return this._leftEnd;
	}
	
	public String getRightEnd(){
		return this._rightEnd;
	}
}
