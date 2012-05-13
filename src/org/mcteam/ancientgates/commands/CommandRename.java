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
		
		try {
			Gate.rename(gate, newId);
		} 
		catch (Exception e) {
			sendMessage("Cannot rename " + gate.getId() + ". There is already a gate named " + newId + ".");
		}
		
		sendMessage("Gate " + gate.getId() + " is now known as " + newId + ".");
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}
