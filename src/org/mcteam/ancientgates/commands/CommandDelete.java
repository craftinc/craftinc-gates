package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Gate;

public class CommandDelete extends BaseCommand {
	public CommandDelete() {
		aliases.add("delete");
		aliases.add("del");
		aliases.add("remove");
		aliases.add("rm");
		
		requiredParameters.add("id");		
		
		senderMustBePlayer = false;
		helpDescription = "Delete a gate";
	}
	
	public void perform() 
	{
		gate.close();
		sendMessage("Gate with id \"" + gate.getId() + "\" was deleted.");
		Gate.delete(gate.getId());
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

