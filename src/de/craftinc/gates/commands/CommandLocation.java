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

import de.craftinc.gates.controllers.PermissionController;
import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import org.bukkit.block.Block;

public class CommandLocation extends BaseLocationCommand {

    public CommandLocation() {
        aliases.add("location");
        aliases.add("lo");

        requiredParameters.add("id");
        helpDescription = "Set the entrance of the gate to your current location.";
        requiredPermission = PermissionController.permissionManage;

        needsPermissionAtCurrentLocation = true;
        shouldPersistToDisk = true;
        senderMustBePlayer = true;
    }

    public void perform() {
        Location playerLocation = getValidPlayerLocation();

        if (playerLocation == null) {
            sendMessage("There is not enough room for a gate to open here");
            return;
        }

        Location oldLocation = gate.getLocation();
        Set<Location> oldGateBlockLocations = gate.getGateBlockLocations();
        Set<Block> oldFrameBlocks = gate.getGateFrameBlocks();

        try {
            if (gate.isOpen()) {
                GateBlockChangeSender.updateGateBlocks(gate, true);
            }

            gate.setLocation(playerLocation);
            sendMessage(ChatColor.GREEN + "The location of '" + gate.getId() + "' is now at your current location.");
        } catch (Exception e) {
            sendMessage(ChatColor.RED + "There seems to be no frame at your new location! The gate got closed!" + ChatColor.AQUA + " You should build a frame now and execute:");
            sendMessage(new CommandTriggerOpen().getUsageTemplate(true));
        } finally {
            gatesManager.handleGateLocationChange(gate, oldLocation, oldGateBlockLocations, oldFrameBlocks);
            GateBlockChangeSender.updateGateBlocks(gate);
        }
    }
}
