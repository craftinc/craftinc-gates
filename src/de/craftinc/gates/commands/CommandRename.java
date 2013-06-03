package de.craftinc.gates.commands;

import org.bukkit.ChatColor;

import de.craftinc.gates.gates.GatesManager;
import de.craftinc.gates.Plugin;


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
		
		helpDescription = "Changes the id of a gate.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = true;
		senderMustBePlayer = false;
	}
	
	
	public void perform() 
	{
		String newId = parameters.get(1);
		GatesManager gatesManager = Plugin.getPlugin().getGatesManager();
		
		if (gatesManager.gateExists(newId)) {
			sendMessage(ChatColor.RED + "Cannot rename " + gate.getId() + ". There is already a gate named " + newId + ".");
		}
		else {
			String oldId = gate.getId();
			
			gate.setId(newId);
			gatesManager.handleGateIdChange(gate, oldId);
			
			sendMessage(ChatColor.GREEN + "Gate " + gate.getId() + " is now known as " + newId + ".");
		}
	}

}
