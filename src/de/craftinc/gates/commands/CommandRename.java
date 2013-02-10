package de.craftinc.gates.commands;

import de.craftinc.gates.Gate;
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
	}
	
	
	public void perform() 
	{
		String newId = parameters.get(1);
		
		try {
			Gate.rename(gate.getId(), newId);
		} 
		catch (Exception e) {
			sendMessage("Cannot rename " + gate.getId() + ". There is already a gate named " + newId + ".");
		}
		
		sendMessage("Gate " + gate.getId() + " is now known as " + newId + ".");
	}

}
