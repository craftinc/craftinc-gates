package de.craftinc.gates.commands;

import de.craftinc.gates.Plugin;


public class CommandClose extends BaseCommand 
{
	public CommandClose()
	{
		aliases.add("close");
		
		requiredParameters.add("id");	
		
		helpDescription = "Closes a gate to prevent players from using it.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
	}
	
	
	@Override
	public void perform() 
	{
		try {
			gate.setOpen(false);
		}
		catch(Exception e) {
		}
		
		sendMessage("The gate was closed.");
	}
}

