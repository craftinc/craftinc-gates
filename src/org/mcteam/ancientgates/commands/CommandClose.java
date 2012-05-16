package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Plugin;


public class CommandClose extends BaseCommand 
{
	public CommandClose()
	{
		aliases.add("close");
		requiredParameters.add("id");		
		helpDescription = "Close that gate";
		requiredPermission = Plugin.permissionManage;
	}
	
	
	@Override
	public void perform() 
	{
		gate.setOpen(false);
		sendMessage("The gate was closed.");
	}
}

