package main;

import java.util.ArrayList;

public class NetworkNode {
	private String _name;
	private int[] _addresses;
	private ArrayList<RouterTableRow> _routerTable;
	public ArrayList<NetworkNode> _linkedNodes;
	private RIPSimulator _parent;
	
	public NetworkNode(String name, int[] addresses, RIPSimulator parent){
		_parent = parent;
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
		/*char newNode = linkedNode.getName().toCharArray()[1];
		
		int indexToInsert = 200;
		boolean indexChanged = false;
		
		for(int i = 0; i < _linkedNodes.size(); i++){
			if(Integer.parseInt(Character.toString(newNode)) < Integer.parseInt(Character.toString(_linkedNodes.get(i).getName().toCharArray()[1]))){
							
							if(i < indexToInsert){
								indexToInsert = i;
								indexChanged = true;
							}
						}
		}
		
		if(indexChanged)
			_linkedNodes.add(indexToInsert, linkedNode);
		else*/
			this._linkedNodes.add(linkedNode);
	}
	
	public void removeLinkedNode(String linkedNodeName){
		
		NetworkNode nodeToRemove = null;
		
		//Find the node to remove
		for(NetworkNode n : _linkedNodes){
			if(n.getName().equals(linkedNodeName)){
				nodeToRemove = n;
			}
		}
		
		//Set the value for that node in the routing table to be invalid
		for(RouterTableRow tr : _routerTable){
			if(tr.getLinkName().equals(linkedNodeName)){
				tr.setCost(Integer.MAX_VALUE);
			}
		}
		
		//Remove the node from linked nodes
		_linkedNodes.remove(nodeToRemove);
		
		//Send updated table to rest of linked nodes
		propagateRoutingTableToLinkedNodes();
	}
	
	public void propagateRoutingTableToLinkedNodes(){
		
		//For each linked node
		for(NetworkNode linkedNode : _linkedNodes){
			System.out.print("send " + this.getName() + " " + linkedNode.getName() + " ");
			
			//Print what is sent
			for(RouterTableRow tr : _routerTable){
				System.out.print("(" + tr.getDestinationAddress() + "|" + ( tr.getCost() == Integer.MAX_VALUE ? "no-link" : tr.getLinkName()) + "|" + ( tr.getCost() == Integer.MAX_VALUE ? "i" : tr.getCost()) + ") ");
			}
			System.out.println();
			
			//Send current node's routing table
			linkedNode.updateRoutingTable(this.getName(), _routerTable);
		}
	}
	
	public void updateRoutingTable(String sender, ArrayList<RouterTableRow> receivedTable){
		boolean localTableUpdated = false;
		//System.out.println("This is: " + this._name);
		//System.out.println("Received table from: " + sender);
		System.out.println(_parent._inputString);
		_parent.printFinalTablesContent();
		/*System.out.println("Router table size: " + _routerTable.size());
		System.out.println("Linked nodes size: " + _linkedNodes.size());*/
		//Print what is received
		System.out.print("receive " + sender + " " + this.getName() + " ");	
		for(RouterTableRow tr: receivedTable){
			System.out.print("(" + tr.getDestinationAddress() + "|" + (tr.getCost() == Integer.MAX_VALUE ? "no-link" : tr.getLinkName()) + "|" + (tr.getCost() == Integer.MAX_VALUE ? "i" : tr.getCost()) + ")");
		}
		
		//RIP algorithm - for each entry in the received table
		for(RouterTableRow tr : receivedTable){
			//address of received entry
			int address = tr.getDestinationAddress(); 
			//cost of received entry
			int cost = tr.getCost();
			
			//If the sender node has connectivity to this address
			if(cost != Integer.MAX_VALUE){		
				boolean addressKnown = false;
				
				//Check if the current node already knows this address
				RouterTableRow localEntryToAddress = null;
				for(RouterTableRow row : _routerTable){
					if(row.getDestinationAddress() == address){
						localEntryToAddress = row;
						addressKnown = true;
					}
				}
				
				//If not add it to the routing table
				if(!addressKnown){
					this.addRoutingTableEntry(address, sender, cost + 1);
					localTableUpdated = true;
				}
				else{
					//If the received entry has better cost to that address than the current one update it
					if((cost + 1) < localEntryToAddress.getCost()){
						localEntryToAddress.setCost(cost+1);
						localEntryToAddress.setLinkName(sender);
						localTableUpdated = true;
					}
					
					//If the current node already has a route to that address through the sender
					if(localEntryToAddress.getLinkName().equals(sender)){
						//And the sender has found a better route update it
						if(cost != (localEntryToAddress.getCost()-1)){
							//System.out.println("This is: " + this._name);
							//System.out.println("\nUpdating address: " + localEntryToAddress.getDestinationAddress() + " throught: " + localEntryToAddress.getLinkName() + " currentCost: " + localEntryToAddress.getCost() + " receivedCost: " + cost);
							localEntryToAddress.setCost(cost+1);
							localTableUpdated = true;
							
							/*if(localEntryToAddress.getCost() > 10 && localEntryToAddress.getCost() != Integer.MAX_VALUE)
								localTableUpdated = false;*/
						}
					}		
				}
			}
			//The sender node has lost connectivity to the address
			else {
				RouterTableRow rowToUpdate = null;
				
				//Check if current node has a route to that address through the sender
				for(RouterTableRow row : _routerTable){
					if(row.getDestinationAddress() == address && row.getLinkName().equals(sender) && row.getCost() != Integer.MAX_VALUE){
						rowToUpdate = row;
					}
				}
				
				//If there is such a route make it invalid
				if(rowToUpdate != null){
					rowToUpdate.setCost(Integer.MAX_VALUE);
					localTableUpdated = true;
				}
				
				//If there is no such route, but the current node has another working route to that address
				//Send it back to the sender node
				
				/*else {
					for(RouterTableRow row : _routerTable){
						if(row.getDestinationAddress() == address && row.getCost() != Integer.MAX_VALUE){
							rowToUpdate = row;
						}
					}
					
					if(rowToUpdate != null){
						localTableUpdated = true;
					}
				}*/
			}
		}
		
		System.out.println();
		
		//If there was any change send it to all neighbouring nodes
		if(localTableUpdated){
			propagateRoutingTableToLinkedNodes();
		}
	}

}
