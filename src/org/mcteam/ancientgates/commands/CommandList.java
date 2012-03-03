package org.mcteam.ancientgates.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.mcteam.ancientgates.Conf;
import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.util.TextUtil;

public class CommandList extends BaseCommand
{
	public CommandList() 
	{
		aliases.add("list");
		aliases.add("ls");
		
		hasGateParam = false;
		
		helpDescription = "Display a list of the gates";
	}
	
	
	public void perform() 
	{
		Collection<Gate> gates = Gate.getAll();
		
		if (gates.size() == 0)
		{
			sendMessage("There are no gates yet.");
			return;
		} 
		
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
			
			ids.get(first).add(Conf.colorAlly + id);
		}
		
		// sort all lists
		for (List<String> list : ids)
			Collections.sort(list);

		
		sendMessage("There are currently " + gates.size() + " gates on this server: ");
		
		for (int i=0; i<ids.size(); i++)
		{
			List<String> currentIds = ids.get(i);
			
			if (currentIds.size() == 0)
				continue;
			
			String head;
			if ( i < 26 )
				head = "" + (char)(i+65) + ":";
			else if ( i == 26 )
				head = "0 - 9:";
			else
				head = "!@#$:";
			
			sendMessage(Conf.colorMember + head);
			sendMessage(TextUtil.implode(currentIds, Conf.colorSystem+", "));
		}
		
	}
}

