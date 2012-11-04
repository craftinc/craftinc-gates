package de.craftinc.gates.commands;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;


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
		helpDescription = "Removes the gate from the game.";
		
		requiredPermission = Plugin.permissionManage;
	}
	
	
	public void perform() 
	{
		Gate.delete(gate.getId());
		sendMessage("Gate with id '" + gate.getId() + "' was deleted.");
	}
}

