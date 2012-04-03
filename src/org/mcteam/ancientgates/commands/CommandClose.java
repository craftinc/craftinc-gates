package org.mcteam.ancientgates.commands;

import org.mcteam.ancientgates.Gate;

public class CommandClose extends BaseCommand {
	
	public CommandClose() {
		aliases.add("close");
		
		requiredParameters.add("id");		
		
		helpDescription = "Close that gate";
	}
	
	public void perform() {
		gate.close();
		
		sendMessage("The gate was closed.");
		
		Gate.save();
	}
}

