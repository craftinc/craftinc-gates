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
import de.craftinc.gates.util.TextUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandHelp extends BaseCommand {

    private static List<String> help;

    public CommandHelp() {
        aliases.add("help");
        aliases.add("?");
        helpDescription = "prints this help page";
        requiredPermission = PermissionController.permissionInfo;
        hasGateParam = false;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = false;
        senderMustBePlayer = false;
    }

    public void perform() {
        sendMessage(TextUtil.titleSize("Craft Inc. Gates Help"));
        sendMessage(getHelp());
    }

    private List<String> getHelp() {
        if (help == null) {
            help = Arrays.asList(
                    new CommandHelp().getUsageTemplate(true),
                    new CommandNew().getUsageTemplate(true),
                    new CommandRemove().getUsageTemplate(true),
                    new CommandLocation().getUsageTemplate(true),
                    new CommandExit().getUsageTemplate(true),
                    new CommandTriggerOpen().getUsageTemplate(true),
                    new CommandRename().getUsageTemplate(true),
                    new CommandList().getUsageTemplate(true),
                    new CommandInfo().getUsageTemplate(true),
                    new CommandNearby().getUsageTemplate(true),
                    new CommandTriggerVehicles().getUsageTemplate(true),
                    new CommandTeleport().getUsageTemplate(true),
                    new CommandMaterial().getUsageTemplate(true)
            );
            Collections.sort(help);
        }

        return help;
    }
}
