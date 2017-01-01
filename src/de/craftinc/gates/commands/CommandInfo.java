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

import de.craftinc.gates.models.Gate;
import de.craftinc.gates.controllers.PermissionController;
import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class CommandInfo extends BaseCommand {

    public CommandInfo() {
        aliases.add("info");
        aliases.add("i");

        optionalParameters.add("id");

        helpDescription = "Print detailed information about a certain or the closest gate.";

        requiredPermission = PermissionController.permissionInfo;

        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = false;
        senderMustBePlayer = false;
        hasGateParam = false;
    }


    public void perform() {
        if (this.parameters.size() > 0) {

            if (!this.setGateUsingParameter(this.parameters.get(0))) {
                sendMessage(ChatColor.RED + "You either provided a invalid gate or do not have permission to " + this.helpDescription.toLowerCase());
                return;
            }

            sendMessage(TextUtil.titleSize("Information about: '" + ChatColor.WHITE + gate.getId() + ChatColor.YELLOW + "'"));
        } else {
            boolean senderIsPlayer = this.sender instanceof Player;

            if (!senderIsPlayer) {
                sendMessage(ChatColor.RED + "Only ingame players can perform this command without a supplied gate id!");
                return;
            }

            Player p = (Player) this.sender;
            this.gate = Plugin.getPlugin().getGatesManager().getNearestGate(p.getLocation());

            if (!this.hasPermission() || this.gate == null) {
                sendMessage(ChatColor.RED + "There is either no gate nearby or you do not have permission to " + this.helpDescription.toLowerCase());
                return;
            }

            Plugin.log(this.gate.toString());

            sendMessage(TextUtil.titleSize("Information about closest gate: '" + ChatColor.WHITE + gate.getId() + ChatColor.YELLOW + "'"));
        }

        String openHiddenMessage = ChatColor.DARK_AQUA + "This gate is";

        if (gate.isOpen())
            openHiddenMessage += ChatColor.AQUA + " open";
        else
            openHiddenMessage += ChatColor.AQUA + " closed";

        if (gate.isHidden())
            openHiddenMessage += ChatColor.DARK_AQUA + " and" + ChatColor.AQUA + " hidden";

        openHiddenMessage += ".\n";

        sendMessage(openHiddenMessage);

        if (gate.getLocation() != null)
            sendMessage(ChatColor.DARK_AQUA + "location: " + ChatColor.AQUA + "( " + (int) gate.getLocation().getX() +
                    " | " + (int) gate.getLocation().getY() + " | " + (int) gate.getLocation().getZ() + " ) in " +
                    gate.getLocation().getWorld().getName());
        else
            sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no location");

        if (gate.getExit() != null)
            sendMessage(ChatColor.DARK_AQUA + "exit:    " + ChatColor.AQUA + "( " + (int) gate.getExit().getX() + " | "
                    + (int) gate.getExit().getY() + " | " + (int) gate.getExit().getZ() + " ) in " +
                    gate.getExit().getWorld().getName());
        else
            sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no exit");


        if (gate.getAllowsVehicles())
            sendMessage(ChatColor.DARK_AQUA + "You can ride through this gate.");


        if (this.sender instanceof Player) {
            HashSet<Gate> set = new HashSet<>();
            set.add(this.gate);

            GateBlockChangeSender.temporaryHighlightGatesFrames((Player)this.sender, set);
        }
    }
}
