package de.craftinc.gates.commands;

import de.craftinc.gates.Plugin;


public class CommandOpen extends BaseCommand 
{
	
	public CommandOpen()
	{
		aliases.add("open");
		
		requiredParameters.add("id");		
		
		helpDescription = "Open a gate so players can use it.";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		try {
			gate.setOpen(true);
		} catch (Exception e) {
			sendMessage(e.getMessage());
			return;
		}
		
		sendMessage("The gate was opened.");
	}

}
