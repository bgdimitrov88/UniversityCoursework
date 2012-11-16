package main;

import java.util.ArrayList;

public class NetworkNode {
	private String _name;
	private int[] _addresses;
	private ArrayList<RouterTableRow> _routerTable;
	private ArrayList<NetworkNode> _linkedNodes;
	
	public NetworkNode(String name, int[] addresses){
		_name = name;
		_addresses = addresses;
		_linkedNodes = new ArrayList<NetworkNode>();
		_routerTable = new ArrayList<RouterTableRow>();
	}
	
	public String getName(){
		return this._name;
	}
	
	public int[] getAddresses(){
		return this._addresses;
	}
	
	public ArrayList<RouterTableRow> getTable(){
		return _routerTable;
	}
	
	public void addRoutingTableEntry(int destinationAddress, String linkName, int cost){
		this._routerTable.add(new RouterTableRow(destinationAddress, linkName, cost));
	}
	
	public void addLinkedNode(NetworkNode linkedNode){
		this._linkedNodes.add(linkedNode);
	}
	
	public void propagateRoutingTableToLinkedNodes(){
		for(NetworkNode linkedNode : _linkedNodes){
			System.out.print("send " + this.getName() + " " + linkedNode.getName() + " ");
			
			for(RouterTableRow tr : _routerTable){
				System.out.print("(" + tr.getDestinationAddress() + "|" + tr.getLinkName() + "|" + tr.getCost() + ") ");
			}
			System.out.println();
			
			linkedNode.updateRoutingTable(this.getName(), _routerTable);
		}
	}
	
	public void updateRoutingTable(String sender, ArrayList<RouterTableRow> receivedTable){
		boolean localTableUpdated = false;
		
		System.out.print("receive " + sender + " " + this.getName() + " ");
		
		for(RouterTableRow tr : receivedTable){
			int address = tr.getDestinationAddress(); 
			int cost = tr.getCost();
			String linkName = tr.getLinkName();
			
			System.out.print("(" + address + "|" + linkName + "|" + cost + ")");
			
			
			boolean addressKnown = false;
			RouterTableRow localEntryToAddress = null;
			for(RouterTableRow row : _routerTable){
				if(row.getDestinationAddress() == address){
					localEntryToAddress = row;
					addressKnown = true;
				}
			}
			
			if(!addressKnown){
				this.addRoutingTableEntry(address, sender, cost + 1);
				localTableUpdated = true;
			}
			else {
				//if(localEntryToAddress != null)
				if((cost + 1) < localEntryToAddress.getCost()){
					localEntryToAddress.setCost(cost);
					localEntryToAddress.setLinkName(sender);
					localTableUpdated = true;
				}
				
				
			}
			
		}
		
		System.out.println();
		
		if(localTableUpdated)
			propagateRoutingTableToLinkedNodes();
	}

}
