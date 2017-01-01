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

import de.craftinc.gates.controllers.PermissionController;
import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;

public class CommandTriggerOpen extends BaseCommand {

    public CommandTriggerOpen() {
        aliases.add("triggerOpen");
        aliases.add("o");

        requiredParameters.add("id");
        helpDescription = "Open/close a gate.";
        requiredPermission = PermissionController.permissionManage;

        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
        senderMustBePlayer = false;
    }

    public void perform() {
        try {
            if (!gate.isOpen() && gate.getExit() == null && player != null) {
                gate.setExit(player.getLocation());
                sendMessage(ChatColor.GREEN + "The exit of gate '" + gate.getId() + "' is now where you stand.");
            }

            gate.setOpen(gate.isOpen());

            GateBlockChangeSender.updateGateBlocks(gate);
            gatesManager.handleGateLocationChange(gate, null, null, null);
            sendMessage(ChatColor.GREEN + "The gate was opened.");
        } catch (Exception e) {
            sendMessage(ChatColor.RED + e.getMessage());
        }
    }
}
