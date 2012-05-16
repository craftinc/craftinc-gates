package org.mcteam.ancientgates.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.util.TextUtil;

public class CommandHelp extends BaseCommand 
{
	
	public CommandHelp() 
	{
		aliases.add("help");
		aliases.add("h");
		aliases.add("?");
		
		optionalParameters.add("page");
		hasGateParam = false;
		
		helpDescription = "Display a help page";
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
		
		sendMessage(TextUtil.titleize("AncientGates Help ("+page+"/"+helpPages.size()+")"));
		
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
		pageLines.add( new CommandSetFrom().getUsageTemplate(true, true) );
		pageLines.add( new CommandSetTo().getUsageTemplate(true, true) );
		pageLines.add( new CommandOpen().getUsageTemplate(true, true) );
		
		helpPages.add(pageLines);
		pageLines = new ArrayList<String>();
		
		pageLines.add( new CommandRename().getUsageTemplate(true, true) );
		pageLines.add( new CommandClose().getUsageTemplate(true, true) );
		pageLines.add( new CommandList().getUsageTemplate(true, true) );
		pageLines.add( new CommandInfo().getUsageTemplate(true, true) );
		pageLines.add( new CommandHide().getUsageTemplate(true, true) );
		pageLines.add( new CommandUnhide().getUsageTemplate(true, true) );
		
		helpPages.add(pageLines);
	}
	
}

