/*  Craft Inc. Gates
    Copyright (C) 2011-2013 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates.commands;


import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;


public class CommandInfo extends BaseCommand 
{
	public CommandInfo()
	{
		aliases.add("info");
		aliases.add("details");
		aliases.add("i");
		aliases.add("d");
		
		requiredParameters.add("id");		
		
		helpDescription = "Print detailed information about a certain gate.";
		
		requiredPermission = Plugin.permissionInfo;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = false;
		senderMustBePlayer = false;
	}
	
	
	public void perform() 
	{
		sendMessage(TextUtil.titleize("Information about: '" + ChatColor.WHITE + gate.getId() + ChatColor.YELLOW + "'"));
		
		String openHiddenMessage = ChatColor.DARK_AQUA + "This gate is";
		
		if (gate.isOpen())
			openHiddenMessage += ChatColor.AQUA + " open";
		else
			openHiddenMessage += ChatColor.AQUA + " closed";
		
		if (gate.isHidden())
			openHiddenMessage += ChatColor.DARK_AQUA +" and" + ChatColor.AQUA + " hidden";
		
		openHiddenMessage += ".\n";
		
		sendMessage(openHiddenMessage);
		
		if (gate.getLocation() != null)
			sendMessage(ChatColor.DARK_AQUA + "from:  " + ChatColor.AQUA + "( " + (int)gate.getLocation().getX() +
                        " | " + (int)gate.getLocation().getY() + " | " + (int)gate.getLocation().getZ() + " ) in " +
                        gate.getLocation().getWorld().getName());
		else
			sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no location");
		
		if (gate.getExit() != null)
			sendMessage(ChatColor.DARK_AQUA + "to:     " + ChatColor.AQUA + "( " + (int)gate.getExit().getX() + " | "
                        + (int)gate.getExit().getY() + " | " + (int)gate.getExit().getZ() + " ) in " +
                        gate.getExit().getWorld().getName());
		else
			sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no exit");


        Plugin.log("frame blocks: " + gate.getGateFrameBlocks());
	}

}
