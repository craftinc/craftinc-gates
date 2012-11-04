package de.craftinc.gates.commands;


import org.bukkit.Location;

import de.craftinc.gates.Plugin;


public class CommandSetLocation extends BaseLocationCommand 
{
	
	public CommandSetLocation() 
	{
		aliases.add("setlocation");
		aliases.add("sl");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set the entrance of the gate to your current location.";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation == null) {
			sendMessage("There is not enough room for a gate to open here");
			return;
		}
		
		try {
			gate.setLocation(playerLocation);
		} 
		catch (Exception e) {
			sendMessage(e.getMessage());
		}
		
		sendMessage("The location of '" + gate.getId() + "' is now at your current location.");
	}
	
}

