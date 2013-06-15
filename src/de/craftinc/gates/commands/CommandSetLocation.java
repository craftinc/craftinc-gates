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


import java.util.Set;
import java.util.logging.Level;

import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import de.craftinc.gates.Plugin;


public class CommandSetLocation extends BaseLocationCommand 
{
	
	public CommandSetLocation() 
	{
		aliases.add("location");
		aliases.add("l");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set the entrance of the gate to your current location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
		shouldPersistToDisk = true;
		senderMustBePlayer = true;
	}
	
	
	public void perform() 
	{
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation == null) 
		{
			sendMessage("There is not enough room for a gate to open here");
			return;
		}
		
		try 
		{
			Location oldLocation = gate.getLocation();
            Set<Location> oldGateBlockLocations = gate.getGateBlockLocations();

            gate.setLocation(playerLocation);
            Plugin.getPlugin().getGatesManager().handleGateLocationChange(gate, oldLocation, oldGateBlockLocations);

			sendMessage(ChatColor.GREEN + "The location of '" + gate.getId() + "' is now at your current location.");
		} 
		catch (Exception e) 
		{
			sendMessage(ChatColor.RED + "Setting the location for the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}

        GateBlockChangeSender.updateGateBlocks(gate);
	}
	
}

