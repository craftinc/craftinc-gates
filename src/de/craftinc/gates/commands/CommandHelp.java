package de.craftinc.gates.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.craftinc.gates.util.TextUtil;

public class CommandHelp extends BaseCommand 
{
	
	public CommandHelp() 
	{
		aliases.add("help");
		aliases.add("h");
		aliases.add("?");
		
		optionalParameters.add("page");
		hasGateParam = false;
		
		helpDescription = "Prints a list of all availible commands.";
	}
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return true;
	}
	
	
	public void perform() 
	{
		int page = 1;
		
		if (parameters.size() > 0) 
		{
			try 
			{
				page = Integer.parseInt(parameters.get(0));
			} 
			catch (NumberFormatException e) 
			{
				// wasn't an integer
			}
		}
		
		sendMessage(TextUtil.titleize("Craft Inc. Gates Help ("+page+"/"+helpPages.size()+")"));
		
		page -= 1;
		if (page < 0 || page >= helpPages.size()) 
		{
			sendMessage("This page does not exist");
			return;
		}
		
		sendMessage(helpPages.get(page));
	}
	
	
	//----------------------------------------------//
	// Build the help pages
	//----------------------------------------------//
	
	public static ArrayList<ArrayList<String>> helpPages;
	
	static 
	{
		helpPages = new ArrayList<ArrayList<String>>();
		ArrayList<String> pageLines;

		pageLines = new ArrayList<String>();
		
		pageLines.add( new CommandHelp().getUsageTemplate(true, true) );
		pageLines.add( new CommandCreate().getUsageTemplate(true, true) );
		pageLines.add( new CommandDelete().getUsageTemplate(true, true) );
		pageLines.add( new CommandSetLocation().getUsageTemplate(true, true) );
		pageLines.add( new CommandSetExit().getUsageTemplate(true, true) );
		pageLines.add( new CommandOpen().getUsageTemplate(true, true) );
		
		helpPages.add(pageLines);
		pageLines = new ArrayList<String>();
		
		pageLines.add( new CommandRename().getUsageTemplate(true, true) );
		pageLines.add( new CommandClose().getUsageTemplate(true, true) );
		pageLines.add( new CommandList().getUsageTemplate(true, true) );
		pageLines.add( new CommandInfo().getUsageTemplate(true, true) );
		pageLines.add( new CommandSetHidden().getUsageTemplate(true, true) );
		pageLines.add( new CommandSetVisible().getUsageTemplate(true, true) );
		
		helpPages.add(pageLines);
	}
	
}

