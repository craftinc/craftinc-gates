package de.craftinc.gates.commands;

import org.bukkit.Location;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;


public class CommandCreate extends BaseLocationCommand 
{
	public CommandCreate() 
	{
		aliases.add("create");
		aliases.add("new");
		
		requiredParameters.add("id");		
		
		senderMustBePlayer = true;
		hasGateParam = false;
		
		helpDescription = "Create a gate at your current location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
	}
	
	
	public void perform() 
	{
		String id = parameters.get(0);
		
		try {
			gate = Gate.create(id);
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation != null) {
			try {
				gate.setLocation(playerLocation);
			} 
			catch (Exception e) {
			}
			
			sendMessage("Gate with id \"" + id + "\" was created.");
			sendMessage("The gates location has been set to your current location.");
		}
		else {
			sendMessage("Gate with id \"" + id + "\" was created.");
			sendMessage("Now you should build a frame and:");
			sendMessage(new CommandSetLocation().getUsageTemplate(true, true));
		}
	}

}

