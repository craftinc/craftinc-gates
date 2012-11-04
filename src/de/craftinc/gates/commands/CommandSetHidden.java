package de.craftinc.gates.commands;

import de.craftinc.gates.Plugin;


public class CommandSetHidden extends BaseCommand 
{
	public CommandSetHidden() 
	{
		aliases.add("setHidden");
		aliases.add("sh");
		
		requiredParameters.add("id");		
		
		helpDescription = "Makes a gate NOT consist of gate blocks while open.";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		try {
			gate.setHidden(true);
		} 
		catch (Exception e) {
			sendMessage(e.getMessage());
		}
		
		sendMessage("The gate '" + gate.getId() + "' is now hidden.");
	}
}