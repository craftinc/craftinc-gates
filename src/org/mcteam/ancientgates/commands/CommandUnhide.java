package org.mcteam.ancientgates.commands;

public class CommandUnhide extends BaseCommand 
{
	
	public CommandUnhide() 
	{
		aliases.add("unhide");
		
		requiredParameters.add("id");		
		
		helpDescription = "Unhide that gate";
	}
	
	public void perform() 
	{
		if (gate.setHidden(false))
			sendMessage("The gate " + gate.getId() + " is no longer hidden.");
		else
			sendMessage("Failed to unhide the gate. Does the portal have a frame?");
	}
}