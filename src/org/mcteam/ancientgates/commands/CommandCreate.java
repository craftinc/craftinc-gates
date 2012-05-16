package org.mcteam.ancientgates.commands;

import org.bukkit.Location;
import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;


public class CommandCreate extends BaseLocationCommand 
{
	public CommandCreate() 
	{
		aliases.add("create");
		aliases.add("new");
		
		requiredParameters.add("id");		
		
		senderMustBePlayer = false;
		hasGateParam = false;
		
		helpDescription = "Create a gate";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		String id = parameters.get(0);
		if (Gate.exists(id)) 
		{
			sendMessage("There gate \"" + id + "\" already exists.");
			return;
		}
		
		gate = Gate.create(id);
		
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation != null) {
			gate.setLocation(playerLocation);
			
			sendMessage("Gate with id \"" + id + "\" was created.\n The gates location has been set to your current location.");
		}
		else {
			sendMessage("Gate with id \"" + id + "\" was created.\n Now you should build a frame and:");
			sendMessage(new CommandSetFrom().getUsageTemplate(true, true));
		}
	}

}

