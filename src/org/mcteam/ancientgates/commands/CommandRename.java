package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Gate;



public class CommandRename extends BaseCommand 
{
	public CommandRename() 
	{
		aliases.add("rename");
		aliases.add("changename");
		aliases.add("cn");
		
		hasGateParam = true;
		senderMustBePlayer = false;
		
		requiredParameters.add("current name");
		requiredParameters.add("new name");
		
		helpDescription = "Change the name of a gate";
	}
	
	
	public void perform() 
	{
		String newId = parameters.get(1);
		String oldId = gate.getId();
		
		if (Gate.exists(newId))
		{
			sendMessage("Cannot rename " + oldId + ". There is already a gate named " + newId + ".");
			return;
		}
		
		Gate.rename(gate, newId);
		
		sendMessage("Gate " + oldId + " is now known as " + newId + ".");
		
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}
