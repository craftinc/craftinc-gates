package de.craftinc.gates.commands;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHelp extends BaseCommand 
{
	public static List<List<String>> helpPages;
	
	static 
	{
		// sort the usage strings
		List<String> allUsageStrings = new ArrayList<String>();
		
		allUsageStrings.add( new CommandHelp().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandCreate().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandDelete().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandSetLocation().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandSetExit().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandOpen().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandRename().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandClose().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandList().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandInfo().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandSetHidden().getUsageTemplate(true, true) );
		allUsageStrings.add( new CommandSetVisible().getUsageTemplate(true, true) );
		
		Collections.sort(allUsageStrings);
		
		
		// put 5 commands on one page
		helpPages = new ArrayList<List<String>>();
		
		while (!allUsageStrings.isEmpty()) {
			int toIndex = allUsageStrings.size() >= 6 ? 5 : allUsageStrings.size();
			List<String> currentHelpPage = new ArrayList<String>(allUsageStrings.subList(0, toIndex));
			helpPages.add(currentHelpPage);
			
			allUsageStrings.removeAll(currentHelpPage);
		}
	}
	
	
	public CommandHelp() 
	{
		aliases.add("help");
		aliases.add("?");
		
		optionalParameters.add("page");
		helpDescription = "prints this help page";
		
		requiredPermission = Plugin.permissionInfo;
		
		hasGateParam = false;
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = false;
		senderMustBePlayer = false;
	}


    public void perform()
    {
		int page;
		
		if (parameters.size() > 0) {
			try {
				page = Integer.parseInt(parameters.get(0));
			} 
			catch (NumberFormatException e) {
				// wasn't an integer
                page = 1;
			}
		}
        else {
            page = 1;
        }
		
		sendMessage(TextUtil.titleize("Craft Inc. Gates Help (" + page + "/" + helpPages.size() + ")"));
		
		page -= 1;
		if (page < 0 || page >= helpPages.size()) {
			sendMessage("This page does not exist");
			return;
		}
		
		sendMessage(helpPages.get(page));
	}
}

