package org.mcteam.ancientgates.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Conf;
import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.util.TextUtil;

public class CommandList extends BaseCommand
{
	public CommandList() 
	{
		aliases.add("list");
		aliases.add("ls");
		
		optionalParameters.add("page");
		hasGateParam = false;
		
		helpDescription = "Display a list of the gates";
	}
	
	
	protected String intToTitleString(int i)
	{
		if ( i < 26 )
			return Conf.colorMember + "" + (char)(i+65) + ":";
		else if ( i == 26 )
			return Conf.colorMember + "0 - 9:";
		else
			return Conf.colorMember + "!@#$:";
	}
	
	
	
	// pages start at 1
	// will return null if requested page not availible
	protected List<String> message(int page)
	{
		Collection<Gate> gates = Gate.getAll();
		
		if (gates.size() == 0)
			return null;
		
		/* sort all gates by there first character
		 * put gates in corresponding Lists
		 * list 0-25: a,b,c, ... ,z
		 * list 26: 0-9
		 * list 27: other
		 */
		List<List<String>> ids = new ArrayList<List<String>>();
		
		for (int i=0; i<28; i++)
			ids.add(new ArrayList<String>());
		
		for (Gate gate : gates)
		{
			String id = gate.getId();
			int first = id.charAt(0);
			
			if (first > 96 && first < 123) // convert lower case chars
				first -= 97;
			else if (first > 64 && first < 91) // convert upper case chars
				first -= 65;
			else if (first > 47 && first < 58) // convert numbers
				first = 26;
			else // everything else
				first = 27;
			
			ids.get(first).add(id);
		}
		
		
		/* calculating which gates will be displayed on which page.
		 * this is a little bit fuzzy. but hopefully it will look
		 * great. (tell me if there is a better way!)
		 */
		
		// list<string>: a list from ids
		// Integer: the number of lines neccessary for displaying the corresponding list
		HashMap<List<String>, Integer> lines = new HashMap<List<String>, Integer>(27);
		
		for (List<String> currentIds : ids)
		{
			if (currentIds.size() == 0)
				continue;
			
			int characters = TextUtil.implode(currentIds, ", ").length();
			lines.put(currentIds, characters / 52 + 2);
		}
		
		
		int currentPage = 1;
		int currentStartingCharList = 0;
		boolean finishedCurrentIds = true;
		
		List<String> pageMessages = new ArrayList<String>();
		
		while (currentStartingCharList < ids.size())
		{
			int linesLeftOnCurrentPage = 9;
			
			while (linesLeftOnCurrentPage > 1 && currentStartingCharList < ids.size())
			{
				List<String> currentIds = ids.get(currentStartingCharList);
				
				if (currentIds.size() > 0)
				{
					// add header line
					if (currentPage == page)
						pageMessages.add(intToTitleString(currentStartingCharList));
					
					//sort
					Collections.sort(currentIds);
				
					// add ids
					if (lines.get(currentIds) <= linesLeftOnCurrentPage) // all ids fit on current page
					{
						linesLeftOnCurrentPage -= lines.get(currentIds);
						
						
						if (currentPage == page)
						{
							pageMessages.add(TextUtil.implode(currentIds, ", "));
							if (finishedCurrentIds == false)
								pageMessages.set(pageMessages.size() -2, pageMessages.get(pageMessages.size() -2) + " (more on previous page)");
						}
						
						finishedCurrentIds = true;
					}
					else // NOT all ids fit on current page
					{
						int charsAvailible = (linesLeftOnCurrentPage - 1) * 52;
						int idsPos = 0;
						
						while (true)
						{
							charsAvailible -= currentIds.get(idsPos).length() + 2;
							
							if (charsAvailible <= 0)
								break;
							
							idsPos++;
						}
						
						List<String> idsToPutOnCurrentPage = currentIds.subList(0, idsPos);
						currentIds.remove(idsToPutOnCurrentPage);
						
						String stringToPutOnCurrentPage = TextUtil.implode(idsToPutOnCurrentPage, ", ");
						
						if (currentPage == page)
						{
							pageMessages.add(stringToPutOnCurrentPage);
							pageMessages.set(pageMessages.size() -2, pageMessages.get(pageMessages.size() -2) + " (more on next page)");
						}
						
						lines.put(currentIds, TextUtil.implode(currentIds, ", ").length() / 52 + 2);
						linesLeftOnCurrentPage -= stringToPutOnCurrentPage.length() / 52 + 2;
						
						finishedCurrentIds = false;
					}
				}
				
				if (finishedCurrentIds)
					currentStartingCharList++;
			}
			
			currentPage++;
		}
		
		if (pageMessages.isEmpty())
			return null;
		else
		{
			ArrayList<String> retVal = new ArrayList<String>();
			retVal.add(ChatColor.LIGHT_PURPLE + "This is page " + ChatColor.WHITE + page + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + --currentPage + ChatColor.LIGHT_PURPLE + ". There are " + gates.size() + " gates on this server: ");
			retVal.addAll(pageMessages);
			
			return retVal;
		}
	}
	
	
	public void perform() 
	{
		Collection<Gate> gates = Gate.getAll();
		
		if (gates.size() == 0)
			sendMessage("There are no gates yet.");
		else
		{
			int page = 1;
			
			try
			{
				page = new Integer(parameters.get(0));
			} 
			catch (Exception e)
			{
			}
			
			List<String> messages = message(page);
			
			if (messages == null)
				sendMessage("The requested page is not availible");
			else
				sendMessage(messages);
		}
		
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionInfo) || sender.hasPermission(permissionManage);
	}
}

