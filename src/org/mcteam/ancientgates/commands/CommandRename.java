package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;


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
