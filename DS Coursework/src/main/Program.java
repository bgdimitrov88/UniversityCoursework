package main;

import java.io.File;

public class Program {
	public static void main(String[] args){
		File f = new File(args[0]);
		RIPSimulator simulator = new RIPSimulator(f);
		//simulator.testInput();
		simulator.runSimulation();
	}
}
