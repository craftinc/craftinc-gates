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

public class CommandUnhide extends BaseCommand {

    public CommandUnhide() {
        aliases.add("unhide");
        aliases.add("u");

        requiredParameters.add("id");
        helpDescription = "Make that gate visible";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
        senderMustBePlayer = false;
    }

    public void perform() {
        try {
            gate.setHidden(false);
            GateBlockChangeSender.updateGateBlocks(gate);
            sendMessage(ChatColor.GREEN + "The gate " + gate.getId() + " is now visible.");
        } catch (Exception e) {
            sendMessage(ChatColor.RED + e.getMessage());
        }
    }
}
