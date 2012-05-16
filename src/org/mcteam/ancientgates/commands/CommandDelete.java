package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;


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
		helpDescription = "Delete a gate";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		gate.setOpen(false);
		Gate.delete(gate.getId());
		
		sendMessage("Gate with id \"" + gate.getId() + "\" was deleted.");
	}
}

