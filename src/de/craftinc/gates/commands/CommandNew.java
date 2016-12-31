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
import org.bukkit.Location;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;

public class CommandNew extends BaseLocationCommand {

    public CommandNew() {
        aliases.add("new");
        aliases.add("n");

        requiredParameters.add("id");

        senderMustBePlayer = true;
        hasGateParam = false;
        helpDescription = "Create a gate at your current location.";
        requiredPermission = Plugin.permissionManage;
        needsPermissionAtCurrentLocation = true;
        shouldPersistToDisk = true;
        senderMustBePlayer = true;
    }

    public void perform() {
        String id = parameters.get(0);
        GatesManager gatesManager = Plugin.getPlugin().getGatesManager();

        if (gatesManager.gateExists(id)) {
            sendMessage(ChatColor.RED + "Creating the gate failed! " + "A gate with the supplied id already exists!");
            return;
        }

        gate = new Gate(id);
        sendMessage(ChatColor.GREEN + "Gate with id '" + id + "' was created.");

        Location playerLocation = getValidPlayerLocation();

        if (playerLocation != null) {
            try {
                gate.setLocation(playerLocation);
                sendMessage(ChatColor.AQUA + "The gates location has been set to your current location.");
            } catch (Exception ignored) {
            }
        } else {
            sendMessage(ChatColor.RED + "Your location is invalid!" + ChatColor.AQUA + "Go somewhere else and execute:");
            sendMessage(new CommandLocation().getUsageTemplate(true));
        }

        gatesManager.handleNewGate(gate);
    }
}
