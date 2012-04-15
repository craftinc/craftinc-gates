package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Gate;

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
		gate.close();
		sendMessage("The gate was closed.");
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

