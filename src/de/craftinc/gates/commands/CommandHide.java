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


public class CommandHide extends BaseCommand {

    public CommandHide() {
        aliases.add("hide");
        aliases.add("h");

        requiredParameters.add("id");

        helpDescription = "Makes a gate NOT consist of gate blocks while open.";

        requiredPermission = Plugin.permissionManage;

        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
        senderMustBePlayer = false;
    }


    public void perform() {
        try {
            gate.setHidden(true);
            GateBlockChangeSender.updateGateBlocks(gate);
            sendMessage(ChatColor.GREEN + "The gate '" + gate.getId() + "' is now hidden.");
        } catch (Exception e) {
            sendMessage(ChatColor.RED + "Hiding the gate failed! See server log for more information");
            Plugin.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
    }
}