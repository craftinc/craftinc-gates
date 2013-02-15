package de.craftinc.gates.commands;


import org.bukkit.ChatColor;
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
		shouldPersistToDisk = true;
	}
	
	
	public void perform() 
	{
		String id = parameters.get(0);
		
		try 
		{
			gate = Gate.create(id);
			sendMessage(ChatColor.GREEN + "Gate with id '" + id + "' was created.");
		} 
		catch (Exception e) 
		{
			sendMessage(ChatColor.RED + "Creating the gate failed!" + e.getMessage() + "See server log for more information");
			return;
		}
		
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation != null) 
		{
			try 
			{
				gate.setLocation(playerLocation);
				sendMessage(ChatColor.AQUA + "The gates location has been set to your current location.");
			} 
			catch (Exception e) 
			{
			}
			
		}
		else 
		{
			sendMessage(ChatColor.GREEN + "Gate with id \"" + id + "\" was created.");
			sendMessage("Now you should build a frame and:");
			sendMessage(new CommandSetLocation().getUsageTemplate(true, true));
		}
	}

}

