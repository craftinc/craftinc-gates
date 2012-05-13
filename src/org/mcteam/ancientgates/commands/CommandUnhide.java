package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;

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
		boolean isOpen = gate.isOpen();
		
		gate.setHidden(false);
		sendMessage("The gate " + gate.getId() + " is no longer hidden.");
		
		if (isOpen != gate.isOpen()) {
			sendMessage("The Portal is now closed. Does the portal have a frame?");
		}
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}