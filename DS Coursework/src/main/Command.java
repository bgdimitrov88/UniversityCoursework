package main;

public class Command {
	private String _commandName;
	private String[] _arguments;
	
	public Command(String commandName, String[] arguments){
		_commandName = commandName;
		_arguments = arguments;
	}
	
	public String getCommandName(){
		return this._commandName;
		
	}
	
	public String[] getArguments(){
		return this._arguments;
	}
	
	public String getName(){
		return this._commandName;
	}

}
