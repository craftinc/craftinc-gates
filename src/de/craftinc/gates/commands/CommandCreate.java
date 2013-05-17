package de.craftinc.gates.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
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
		
		senderMustBePlayer = true;
	}
	
	
	public void perform() 
	{
		String id = parameters.get(0);
		GatesManager gatesManager = Plugin.getPlugin().getGatesManager();
		
		if (gatesManager.gateExists(id)) {
			sendMessage(ChatColor.RED + "Creating the gate failed!" + "A gate with the supplied id already exists!");
			return;
		}
		
		gate = new Gate(id);
		gatesManager.handleNewGate(gate);
		sendMessage(ChatColor.GREEN + "Gate with id '" + id + "' was created.");

		
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation != null) {
			
			try {
				gate.setLocation(playerLocation);
				sendMessage(ChatColor.AQUA + "The gates location has been set to your current location.");
			} 
			catch (Exception e) {}
		}
		else 
		{
			sendMessage("Now you should build a frame and execute:");
			sendMessage(new CommandSetLocation().getUsageTemplate(true, true));
		}
	}
}

