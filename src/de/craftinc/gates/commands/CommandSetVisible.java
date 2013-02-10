package de.craftinc.gates.commands;

import de.craftinc.gates.Plugin;


public class CommandSetVisible extends BaseCommand 
{
	
	public CommandSetVisible() 
	{
		aliases.add("makevisible");
		aliases.add("mv");
		
		requiredParameters.add("id");		
		
		helpDescription = "Make that gate visible";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
	}
	
	
	public void perform() 
	{
		try {
			gate.setHidden(false);
		}
		catch (Exception e) {
			sendMessage(e.getMessage());
		}
		
		sendMessage("The gate " + gate.getId() + " is now visible.");
	}

}