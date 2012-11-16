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
						
						_networkNodes.add(new NetworkNode(nodeName, nodeAddresses));
					}
					
					if(inputData[0].equals("link")){
						String leftEnd = inputData[1];
						String rightEnd = inputData[2];
						_networkLinks.add(new NetworkLink(leftEnd, rightEnd));
					}
					
					if(inputData[0].equals("send")){
						String processName = inputData[1];
						_commands.add(new Command("send", processName));
					}
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
				}*/
				
			} catch (FileNotFoundException e) {
				System.out.println("The specified file was not found.");
			}
		}
}
