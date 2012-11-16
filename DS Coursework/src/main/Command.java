package main;

public class Command {
	private String _commandName;
	private String _processName;
	
	public Command(String commandName, String processName){
		_commandName = commandName;
		_processName = processName;
	}
	
	public String getCommandName(){
		return this._commandName;
		
	}
	
	public String getProcessName(){
		return this._processName;
	}

}
