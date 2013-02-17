package de.craftinc.gates.commands;

import org.bukkit.ChatColor;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;


public class CommandDelete extends BaseCommand 
{
	public CommandDelete() 
	{
		aliases.add("delete");
		aliases.add("del");
		aliases.add("remove");
		aliases.add("rm");
		
		requiredParameters.add("id");		
		
		senderMustBePlayer = false;
		helpDescription = "Removes the gate from the game.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = true;
		
		senderMustBePlayer = false;
	}
	
	
	public void perform() 
	{
		Gate.delete(gate.getId());
		sendMessage(ChatColor.GREEN + "Gate with id '" + gate.getId() + "' was deleted.");
	}
}

