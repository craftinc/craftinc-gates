package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Plugin;


public class CommandClose extends BaseCommand 
{
	public CommandClose()
	{
		aliases.add("close");
		
		requiredParameters.add("id");	
		
		helpDescription = "Closes a gate to prevent players from using it.";
		
		requiredPermission = Plugin.permissionManage;
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

