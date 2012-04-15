package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Gate;

public class CommandCreate extends BaseCommand 
{
	public CommandCreate() 
	{
		aliases.add("create");
		aliases.add("new");
		
		requiredParameters.add("id");		
		
		senderMustBePlayer = false;
		hasGateParam = false;
		
		helpDescription = "Create a gate";
	}
	
	
	public void perform() 
	{
		String id = parameters.get(0);
		if (Gate.exists(id)) 
		{
			sendMessage("There gate \"" + id + "\" already exists.");
			return;
		}
		
		Gate.create(id);
		sendMessage("Gate with id \"" + id + "\" was created. Now you should:");
		sendMessage(new CommandSetFrom().getUsageTemplate(true, true));
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

