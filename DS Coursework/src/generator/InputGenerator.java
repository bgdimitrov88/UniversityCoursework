package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.lang.StringBuilder;

public class InputGenerator {
File _inputFile;
	
	public InputGenerator(File f){
		_inputFile = f;
	}
	
	public String run(){
		//try{
		StringBuilder sb = new StringBuilder();
		/*_inputFile.delete();
		_inputFile.createNewFile();
		FileWriter commandsFile = new FileWriter(_inputFile);
		BufferedWriter bw = new BufferedWriter(commandsFile);*/
		
		Random r = new Random();
		
		ArrayList<String> generatedNodes = new ArrayList<String>();
		ArrayList<String> generatedLinks = new ArrayList<String>();
		ArrayList<String> generatedFailedLinks = new ArrayList<String>();
		
		int iterations = r.nextInt(5);
		if(iterations < 2)
			iterations += 3;
		//Add between 3 and 10 nodes
		for(int i = 0; i <= iterations; i++){
				String s = "node p" + i + " " + i + "\n";
				generatedNodes.add(s);
		}
		
		for(String node : generatedNodes){
			sb.append(node);
			/*bw.write(node);
			bw.newLine();*/
		}
		
		//Get a random number of iterations between the least possible and max possible number of links for the generated node count
		iterations = r.nextInt(getMostPossibleLinks(generatedNodes.size()));
		int leastPossibleLinks = getLeastPossibleLinks(generatedNodes.size());
		while(iterations < leastPossibleLinks){
			iterations = r.nextInt(getMostPossibleLinks(generatedNodes.size()));
		}
		
		boolean stopAddingLinks = false;
		
		while(!stopAddingLinks){
			stopAddingLinks = true;
			
			for(int i = 0; i < iterations; i++){
				boolean successfullyAdded = false;
				while(!successfullyAdded){
					int leftNode = r.nextInt(generatedNodes.size());
					int rightNode = leftNode;
					while(rightNode == leftNode){
						rightNode = r.nextInt(generatedNodes.size());
					}
					
					String s = "link " + "p" + leftNode + " p" + rightNode + "\n";
					String reverse = "link " + "p" + rightNode + " p" + leftNode + "\n";
					
					if(!generatedLinks.contains(s) && !generatedLinks.contains(reverse)){
						generatedLinks.add(s);
						successfullyAdded = true;
					}
				}
			}
			
			//Check if all possible combinations have been added
			for(String node : generatedNodes){
				boolean nodePresentInLinks = false;
				for(String link : generatedLinks){
					if(link.contains(node))
						nodePresentInLinks = true;
				}
				
				if(nodePresentInLinks)
					stopAddingLinks = false;
			}
			
		}
		
		for(String link : generatedLinks){
			sb.append(link);
			/*bw.write(link);
			bw.newLine();*/
		}
		
		sb.append("send p0\n");
		
		int maxNumFailedLinks = generatedLinks.size() - getLeastPossibleLinks(generatedNodes.size());
		
		if(maxNumFailedLinks > 0){
			iterations = r.nextInt(maxNumFailedLinks+1);
			
			while(iterations == 0){
				iterations = r.nextInt(maxNumFailedLinks+1);
			}
		}
		else {
			iterations = 0;
		}
			
		
		for(int i = 0; i < iterations; i++){
			boolean successfullyAdded = false;
			while(!successfullyAdded){
				int leftNode = r.nextInt(generatedNodes.size());
				int rightNode = leftNode;
				while(rightNode == leftNode){
					rightNode = r.nextInt(generatedNodes.size());
				}
				
				String s = "link-fail " + "p" + leftNode + " p" + rightNode + "\n";
				String sLink = "link " + "p" + leftNode + " p" + rightNode + "\n";
				String reverse = "link-fail " + "p" + rightNode + " p" + leftNode + "\n";
				String reverseLink = "link " + "p" + rightNode + " p" + leftNode + "\n";
				//System.out.println("Trying: " + s);
				if(!generatedFailedLinks.contains(s) && !generatedFailedLinks.contains(reverse)){
					if(generatedLinks.contains(sLink) || generatedLinks.contains(reverseLink)){
						if(generatedLinks.contains(sLink))
							generatedLinks.remove(sLink);
						
						if(generatedLinks.contains(reverseLink))
							generatedLinks.remove(reverseLink);
						

						boolean thereIsACutoutNode = false;
						for(String node : generatedNodes){
							boolean nodeNotPresentInLinks = false;
							for(String link : generatedLinks){
								if(link.contains(node))
									nodeNotPresentInLinks = true;
							}
							
							if(nodeNotPresentInLinks)
								thereIsACutoutNode = true;
						}
						
						if(!thereIsACutoutNode){
							generatedFailedLinks.add(s);
							successfullyAdded = true;
						}
						else{
							iterations++;
						}
					}
				}
			}
		}
		
		for(String failedLink : generatedFailedLinks){
			sb.append(failedLink);
			/*bw.write(link);
			bw.newLine();*/
		}
		
		/*bw.newLine();
		bw.flush();
		bw.close();*/
		
		/*} catch(IOException e){
			e.printStackTrace();
		}*/
		
		//System.out.print(sb.toString());
		return sb.toString();
	}
	
	int getMostPossibleLinks(int n){	
		if(n == 1)
			return 0;
		else
			return getMostPossibleLinks(n-1) + (n -1);
	}
	
	int getLeastPossibleLinks(int n){
		return n-1;
	}
}
