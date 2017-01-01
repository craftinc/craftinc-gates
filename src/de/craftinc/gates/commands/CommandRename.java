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
import org.bukkit.ChatColor;

public class CommandRename extends BaseCommand {

    public CommandRename() {
        aliases.add("rename");
        aliases.add("rn");

        hasGateParam = true;
        senderMustBePlayer = false;
        requiredParameters.add("current name");
        requiredParameters.add("new name");
        helpDescription = "Changes the id of a gate.";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
    }

    public void perform() {
        String newId = parameters.get(1);

        if (gatesManager.gateExists(newId)) {
            sendMessage(ChatColor.RED + "Cannot rename " + gate.getId() + ". There is already a gate named " + newId + ".");
        } else {
            String oldId = gate.getId();

            gate.setId(newId);
            gatesManager.handleGateIdChange(gate, oldId);

            sendMessage(ChatColor.GREEN + "Gate " + gate.getId() + " is now known as " + newId + ".");
        }
    }
}
