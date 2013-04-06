package de.craftinc.gates.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;


public class CommandList extends BaseCommand
{
	public CommandList() 
	{
		aliases.add("list");
		aliases.add("ls");
		
		optionalParameters.add("page");
		hasGateParam = false;
		needsPermissionAtCurrentLocation = false;
		
		helpDescription = "lists all availible gates.";
		
		requiredPermission = Plugin.permissionInfo;
		shouldPersistToDisk = false;
		senderMustBePlayer = false;
	}
	
	
	protected static String intToTitleString(int i)
	{
		if ( i < 26 ) {
			return ChatColor.DARK_AQUA + "" + (char)(i+65) + ":";
		}
		else if ( i == 26 ) {
			return ChatColor.DARK_AQUA + "0 - 9:";
		} 
		else {
			return ChatColor.DARK_AQUA + "!@#$:";
		}
	}
	
	
	/**
	 * Method for returning a collection of gates the player is allowed to see.
	 */
	protected Collection<Gate> getAllGates()
	{
		Collection<Gate> gates = Gate.getAll();
		
		if (this.sender instanceof Player && Plugin.permission != null)
		{
			Player p = (Player)this.sender;
			Collection<Gate> gatesCopy = new ArrayList<Gate>(gates); // create a copy since we cannot iterate over a collection while modifying it!
			
			for (Gate gate : gatesCopy) {
				
				boolean permissionAtGateLocation = Plugin.permission.has(gate.getLocation().getWorld(), p.getName(), this.requiredPermission);
				
				if (!permissionAtGateLocation) 
				{
					gates.remove(gate);
					continue;
				}
				
				boolean permissionAtGateExit = Plugin.permission.has(gate.getExit().getWorld(), p.getName(), this.requiredPermission);
				if (gate.getExit() != null && !permissionAtGateExit) 
				{
					gates.remove(gate);
				}
			}
		}
		
		return gates;
	}
	
	
	/* sort all gates by there first character
	 * put gates in corresponding Lists
	 * all returned lists will be sorted alphabetically
	 * list 0-25: a,b,c, ... ,z
	 * list 26: 0-9
	 * list 27: other
	 */
	protected List<List<String>> gatesSortedByName(Collection<Gate> allGates)
	{
		List<List<String>> ids = new ArrayList<List<String>>();
		
		for (int i=0; i<28; i++) {
			ids.add(new ArrayList<String>());
		}
		
		for (Gate gate : allGates) {
			String id = gate.getId();
			int first = id.charAt(0);
			
			if (first > 96 && first < 123) { // convert lower case chars
				first -= 97;
			}
			else if (first > 64 && first < 91)  { // convert upper case chars
				first -= 65;
			}
			else if (first > 47 && first < 58) { // convert numbers
				first = 26;
			}
			else { // everything else
				first = 27;
			}
			
			ids.get(first).add(id);
		}
		
		for (int i=0; i<28; i++) {
			Collections.sort(ids.get(i));
		}
		
		return ids;
	}
	
	
	// pages start at 1
	// will return null if requested page not availible
	protected List<String> message(int page)
	{
		Collection<Gate> gates = this.getAllGates();
		
		if (gates.size() == 0) {
			return null;
		}
		
		List<List<String>> ids = gatesSortedByName(gates);
		
		/* calculating which gates will be displayed on which page.
		 * this is a little bit fuzzy. but hopefully it will look
		 * great. (tell me if there is a better way!)
		 */
		int currentPage = 1;
		int currentStartingCharList = 0;
		boolean finishedCurrentIds = true;
		
		List<String> pageMessages = new ArrayList<String>();
		
		while (currentStartingCharList < ids.size()) {
			int linesLeftOnCurrentPage = 9;
			
			while (linesLeftOnCurrentPage > 1 && currentStartingCharList < ids.size()) {
				List<String> currentIds = ids.get(currentStartingCharList);
				
				if (currentIds.size() > 0) {
					// add header line
					if (currentPage == page) {
						pageMessages.add(intToTitleString(currentStartingCharList));
					}
				
					// add ids
					int numLinesForCurrentChar = TextUtil.implode(currentIds, ", ").length() / 52 + 2;
					
					if (numLinesForCurrentChar <= linesLeftOnCurrentPage) { // all ids fit on current page
						linesLeftOnCurrentPage -= numLinesForCurrentChar;
						
						if (currentPage == page) {
							pageMessages.add(ChatColor.AQUA + TextUtil.implode(currentIds, ", "));
							if (finishedCurrentIds == false) {
								pageMessages.set(pageMessages.size() -2, pageMessages.get(pageMessages.size() -2) + " (more on previous page)");
							}
						}
						
						finishedCurrentIds = true;
					}
					else { // NOT all ids fit on current page
						int charsAvailible = (linesLeftOnCurrentPage - 1) * 52;
						int idsPos = 0;
						
						do {
							charsAvailible -= currentIds.get(idsPos).length() + 2;				
							idsPos++;
						} while (charsAvailible > 0);
						
						List<String> idsToPutOnCurrentPage = currentIds.subList(0, idsPos);
						currentIds.remove(idsToPutOnCurrentPage);
						
						String stringToPutOnCurrentPage = TextUtil.implode(idsToPutOnCurrentPage, ", ");
						
						if (currentPage == page) {
							pageMessages.add(ChatColor.AQUA + stringToPutOnCurrentPage);
							pageMessages.set(pageMessages.size() -2, pageMessages.get(pageMessages.size() -2) + " (more on next page)");
						}
						
						linesLeftOnCurrentPage -= stringToPutOnCurrentPage.length() / 52 + 2;
						
						finishedCurrentIds = false;
					}
				}
				
				if (finishedCurrentIds) {
					currentStartingCharList++;
				}
			}
			
			currentPage++;
		}
		
		if (pageMessages.isEmpty()) {
			return null;
		} 
		else {
			ArrayList<String> retVal = new ArrayList<String>();
			retVal.add(TextUtil.titleize("List of all gates (" + page + "/" + --currentPage + ")"));
			retVal.addAll(pageMessages);
			
			return retVal;
		}
	}
	
	
	protected int getPageParameter()
	{
		int page = 1;
		
		try {
			page = new Integer(parameters.get(0));
		} 
		catch (Exception e) { }
		
		return page;
	}
	
	
	public void perform() 
	{
		int page = this.getPageParameter();
		
		List<String> messages = message(page);
		
		if (messages == null) {	
			if (page == 1) { // no gates exist
				sendMessage(ChatColor.RED + "There are no gates yet. " + ChatColor.RESET + 
						    "(Note that you might not be allowed to get information about certain gates)");
			}
			else { // the requested page does not exist
				sendMessage(ChatColor.RED + "The requested page is not availible");
			}
		}
		else {
			sendMessage(messages);
		}
	}
}

