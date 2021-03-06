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

public class CommandTriggerVehicles extends BaseCommand {

    public CommandTriggerVehicles() {
        aliases.add("vehicles");
        aliases.add("v");

        requiredParameters.add("id");

        helpDescription = "Allow/deny players to travel while riding.";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
        senderMustBePlayer = false;
    }

    @Override
    protected void perform() {
        gate.setAllowsVehicles(gate.getAllowsVehicles());

        if (gate.getAllowsVehicles()) {
            sendMessage(ChatColor.GREEN + "Traveling while riding is now enabled for this gate.");
        } else {
            sendMessage(ChatColor.GREEN + "Traveling while riding is now disabled for this gate.");
        }
    }
}
