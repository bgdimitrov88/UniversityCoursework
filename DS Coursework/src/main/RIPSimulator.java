package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RIPSimulator {
	
		private File _inputFile;
		private ArrayList<NetworkNode> _networkNodes;
		private ArrayList<NetworkLink> _networkLinks;
		private ArrayList<Command> _commands;
		
		public RIPSimulator(File inputFile){
			_inputFile = inputFile;
			_networkNodes = new ArrayList<NetworkNode>();
			_networkLinks = new ArrayList<NetworkLink>();
			_commands = new ArrayList<Command>();
		}
		
		public void runSimulation(){
			try {
				Scanner s = new Scanner(_inputFile);
				
				while(s.hasNextLine()){
					String inputLine = s.nextLine();
					String[] inputData = inputLine.split(" ");
					
					if(inputData[0].equals("node")){
						String nodeName = inputData[1];
						int[] nodeAddresses = new int[inputData.length - 2];
						
						for(int i = 2; i < inputData.length; i++){
							nodeAddresses[i-2] = Integer.parseInt(inputData[i]);
						}
						
						//Create new node
						NetworkNode newNetworkNode = new NetworkNode(nodeName, nodeAddresses);
						
						//Add entries in node's routing table for its own addresses
						for(int i = 0; i < nodeAddresses.length; i++){
							newNetworkNode.addRoutingTableEntry(nodeAddresses[i], "local", 0);
						}
						
						_networkNodes.add(newNetworkNode);
					}
					
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
						
						if(leftEndNode != null)
							leftEndNode.addLinkedNode(rightEndNode);
						
						if(rightEndNode != null)
							rightEndNode.addLinkedNode(leftEndNode);
						
						_networkLinks.add(new NetworkLink(leftEnd, rightEnd));
					}
					
					if(inputData[0].equals("send")){
						String processName = inputData[1];
						_commands.add(new Command("send", processName));
					}
				}
				s.close();
			} catch (FileNotFoundException e) {
				System.out.println("The specified file was not found.");
			}
			
			for(Command c : _commands){
				String initiatingProcess = c.getProcessName();
				
				NetworkNode initiatingNode = null;
				
				for(NetworkNode n : _networkNodes){
					if(n.getName().equals(initiatingProcess))
						initiatingNode = n;
				}
				
				if(initiatingNode != null)
					initiatingNode.propagateRoutingTableToLinkedNodes();
			}
				
			//Verify input
			/*for(NetworkNode n : _networkNodes){
				System.out.print("node " + n.getName() + " ");
				
				for(int i : n.getAddresses()){
					System.out.print(i + " ");
				}
				
				System.out.println();
			}
			
			for(NetworkLink l : _networkLinks){
				System.out.println("link " + l.getLeftEnd() + " " + l.getRightEnd());
			}
			
			for(Command c : _commands){
				System.out.println(c.getCommandName() + " " + c.getProcessName());
			}
			
			for(NetworkNode n : _networkNodes){
				ArrayList<RouterTableRow> nodeRoutingTable = n.getTable();
				
				for(RouterTableRow tr : nodeRoutingTable){
					System.out.print(n.getName() + ": ");
					System.out.print("(" + tr.getDestinationAddress() + "|" + tr.getLinkName() + "|" + tr.getCost() + ") ");
					System.out.println();
				}
			}*/
		}
}
