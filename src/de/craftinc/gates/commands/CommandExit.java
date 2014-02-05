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

import java.util.logging.Level;

import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;
import org.bukkit.Location;


public class CommandExit extends BaseCommand
{
	public CommandExit()
	{
		aliases.add("exit");
		aliases.add("e");
		
		requiredParameters.add("id");		
		
		helpDescription = "Change exit of location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
		shouldPersistToDisk = true;
		senderMustBePlayer = true;
	}
	
	
	public void perform() 
	{
		try 
		{
            Location oldExit = gate.getExit();
			gate.setExit(player.getLocation());
			sendMessage(ChatColor.GREEN + "The exit of gate '" + gate.getId() + "' is now where you stand.");
            Plugin.getPlugin().getGatesManager().handleGateExitChange(gate, oldExit);
		} 
		catch (Exception e) {
            GateBlockChangeSender.updateGateBlocks(gate);
            sendMessage(ChatColor.RED + "Setting the exit for the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
}

