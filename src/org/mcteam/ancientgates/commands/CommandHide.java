package org.mcteam.ancientgates.commands;

public class CommandHide extends BaseCommand 
{
	
	public CommandHide() 
	{
		aliases.add("hide");
		
		requiredParameters.add("id");		
		
		helpDescription = "Hide that gate";
	}
	
	public void perform() 
	{
		gate.setHidden(true);
		sendMessage("The gate " + gate.getId() + " is now hidden.");
	}
}