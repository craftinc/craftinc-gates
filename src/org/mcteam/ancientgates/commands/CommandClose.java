package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;

public class CommandClose extends BaseCommand 
{
	public CommandClose()
	{
		aliases.add("close");
		
		requiredParameters.add("id");		
		
		helpDescription = "Close that gate";
	}
	
	@Override
	public void perform() 
	{
		gate.setOpen(false);
		sendMessage("The gate was closed.");
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

