package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Plugin;


public class CommandSetExit extends BaseCommand 
{
	
	public CommandSetExit() 
	{
		aliases.add("setto");
		aliases.add("st");
		
		requiredParameters.add("id");		
		
		helpDescription = "Changes the location where the gate will teleport players to your current location.";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		try {
			gate.setExit(player.getLocation());
		} 
		catch (Exception e) {
			sendMessage(e.getMessage());
		}
		
		sendMessage("The exit of gate '" + gate.getId() + "' is now where you stand.");
	}
}

