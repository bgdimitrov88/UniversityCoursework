package main;

import generator.InputGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RIPSimulator {
	
		private File _inputFile;
		public String _inputString;
		private ArrayList<NetworkNode> _networkNodes;
		private ArrayList<NetworkLink> _networkLinks;
		private ArrayList<Command> _commands;
		
		public RIPSimulator(File inputFile){
			_inputFile = inputFile;
			_networkNodes = new ArrayList<NetworkNode>();
			_networkLinks = new ArrayList<NetworkLink>();
			_commands = new ArrayList<Command>();
		}
		
		public void testInput(){
			
			for(int i = 0; i < 10000; i++){
			_networkNodes.clear();
			_networkLinks.clear();
			_commands.clear();
			InputGenerator generator = new InputGenerator(_inputFile);
			_inputString = generator.run();
			System.out.println(_inputString);
			
			/*try {
				Scanner s = new Scanner(_inputFile);
				while(s.hasNextLine()){
					System.out.println(s.nextLine());
				}
				s.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/
			
			this.runSimulation();
			
			}
		}
		
		public void runSimulation(){
			try {						
				
				Scanner s = new Scanner(_inputFile);
				
				//Scan the input file line by line and extract the input data
				while(s.hasNextLine()){
					String inputLine = s.nextLine();
					String[] inputData = inputLine.split(" ");
					
					//Extract information about the nodes
					if(inputData[0].equals("node")){
						String nodeName = inputData[1];
						int[] nodeAddresses = new int[inputData.length - 2];
						
						for(int i = 2; i < inputData.length; i++){
							nodeAddresses[i-2] = Integer.parseInt(inputData[i]);
						}
						
						//Create new node
						NetworkNode newNetworkNode = new NetworkNode(nodeName, nodeAddresses, this);
						
						//Add entries in node's routing table for its own local addresses
						for(int i = 0; i < nodeAddresses.length; i++){
							newNetworkNode.addRoutingTableEntry(nodeAddresses[i], "local", 0);
						}
						
						//Add the new node to the list of nodes
						_networkNodes.add(newNetworkNode);
					}
					
					//Extract information about the links between the nodes
					if(inputData[0].equals("link")){
						String leftEnd = inputData[1];
						String rightEnd = inputData[2];
						
						NetworkNode leftEndNode = null;
						NetworkNode rightEndNode = null;
						
						for(NetworkNode n : _networkNodes){
							if(n.getName().equals(leftEnd))
								leftEndNode = n;
							else if(n.getName().equals(rightEnd))
								rightEndNode = n;
						}
						
						//Add each node at either side of the link to the other side's list of linked nodes
						if(leftEndNode != null)
							leftEndNode.addLinkedNode(rightEndNode);
						
						if(rightEndNode != null)
							rightEndNode.addLinkedNode(leftEndNode);
						
						//Add the link to the list of links
						_networkLinks.add(new NetworkLink(leftEnd, rightEnd));
					}
					
					//Extract information about the commands
					if(inputData[0].equals("send")){
						String processName = inputData[1];
						_commands.add(new Command("send", new String[]{processName}));
					}
					else if(inputData[0].equals("link-fail")){
						String leftNode = inputData[1];
						String rightNode = inputData[2];
						_commands.add(new Command("link-fail", new String[]{leftNode, rightNode}));
					}
				}
				s.close();
			} catch (FileNotFoundException e) {
				System.out.println("The specified file was not found.");
			} 
			
			/*for(NetworkNode n : _networkNodes){
				System.out.print("Node order: ");
				for(NetworkNode tr : n._linkedNodes){
					System.out.print(tr.getName() + " ");
				}
				System.out.println();
			}*/
			
			//Execute the commands
			for(Command c : _commands){
				String[] commandArguments = c.getArguments();
				
				NetworkNode node = null;
				
				//If 'send' command find the initiating node and sends its table to all linked nodes
				if(c.getName().equals("send")){
					for(NetworkNode n : _networkNodes){
						if(n.getName().equals(commandArguments[0]))
							node = n;
					}
					
					if(node != null)
						node.propagateRoutingTableToLinkedNodes();
				}
				//If 'link-fail' command remove each node at either side of the link from the other side's list of linked nodes
				else if(c.getName().equals("link-fail")){
					//printFinalTablesContent();
					String leftNode = commandArguments[0];
					String rightNode = commandArguments[1];
					
					System.out.println("link-fail " + leftNode + " " + rightNode);
					
					for(NetworkNode n : _networkNodes){
						if(n.getName().equals(leftNode))
							node = n;
					}
					
					//The removal of a link causes the node to send its table to all neighbouring nodes - see NetworkNode class
					node.removeLinkedNode(rightNode);

					//printFinalTablesContent();
					System.out.println("link-fail " + rightNode + " " + leftNode);
					
					for(NetworkNode n : _networkNodes){
						if(n.getName().equals(rightNode))
							node = n;
					}
					
					//The removal of a link causes the node to send its table to all neighbouring nodes - see NetworkNode class
					node.removeLinkedNode(leftNode);
				}
			}
			
			//Print final status of nodes' tables
			for(NetworkNode n : _networkNodes){
				ArrayList<RouterTableRow> nodeRoutingTable = n.getTable();
				
				System.out.print("table " + n.getName() + " ");
				for(RouterTableRow tr : nodeRoutingTable){
					System.out.print("(" + tr.getDestinationAddress() + "|" + (tr.getCost() == Integer.MAX_VALUE ? "no-link" : tr.getLinkName()) + "|" + (tr.getCost() == Integer.MAX_VALUE ? "i" : tr.getCost() ) + ") ");					
				}
				System.out.println();
			}
		}
		
		public void printFinalTablesContent(){
			for(NetworkNode n : _networkNodes){
				ArrayList<RouterTableRow> nodeRoutingTable = n.getTable();
				
				System.out.print("table " + n.getName() + " ");
				for(RouterTableRow tr : nodeRoutingTable){
					System.out.print("(" + tr.getDestinationAddress() + "|" + (tr.getCost() == Integer.MAX_VALUE ? "no-link" : tr.getLinkName()) + "|" + (tr.getCost() == Integer.MAX_VALUE ? "i" : tr.getCost() ) + ") ");					
				}
				System.out.println();
			}
		}
}
