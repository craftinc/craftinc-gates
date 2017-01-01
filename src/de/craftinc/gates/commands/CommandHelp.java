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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHelp extends BaseCommand {

    private static List<List<String>> helpPages;

    static {
        // sort the usage strings
        List<String> allUsageStrings = new ArrayList<>();

        allUsageStrings.add(new CommandHelp().getUsageTemplate(true));
        allUsageStrings.add(new CommandNew().getUsageTemplate(true));
        allUsageStrings.add(new CommandRemove().getUsageTemplate(true));
        allUsageStrings.add(new CommandLocation().getUsageTemplate(true));
        allUsageStrings.add(new CommandExit().getUsageTemplate(true));
        allUsageStrings.add(new CommandOpen().getUsageTemplate(true));
        allUsageStrings.add(new CommandRename().getUsageTemplate(true));
        allUsageStrings.add(new CommandClose().getUsageTemplate(true));
        allUsageStrings.add(new CommandList().getUsageTemplate(true));
        allUsageStrings.add(new CommandInfo().getUsageTemplate(true));
        allUsageStrings.add(new CommandHide().getUsageTemplate(true));
        allUsageStrings.add(new CommandUnhide().getUsageTemplate(true));
        allUsageStrings.add(new CommandExitOpen().getUsageTemplate(true));
        allUsageStrings.add(new CommandNearby().getUsageTemplate(true));

        Collections.sort(allUsageStrings);


        // put 5 commands on one page
        helpPages = new ArrayList<>();

        while (!allUsageStrings.isEmpty()) {
            int toIndex = allUsageStrings.size() >= 6 ? 5 : allUsageStrings.size();
            List<String> currentHelpPage = new ArrayList<>(allUsageStrings.subList(0, toIndex));
            helpPages.add(currentHelpPage);

            allUsageStrings.removeAll(currentHelpPage);
        }
    }


    public CommandHelp() {
        aliases.add("help");
        aliases.add("?");

        optionalParameters.add("page");
        helpDescription = "prints this help page";

        requiredPermission = PermissionController.permissionInfo;

        hasGateParam = false;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = false;
        senderMustBePlayer = false;
    }


    public void perform() {
        int page;

        if (parameters.size() > 0) {
            try {
                page = Integer.parseInt(parameters.get(0));
            } catch (NumberFormatException e) {
                // wasn't an integer
                page = 1;
            }
        } else {
            page = 1;
        }

        sendMessage(TextUtil.titleSize("Craft Inc. Gates Help (" + page + "/" + helpPages.size() + ")"));

        page -= 1;
        if (page < 0 || page >= helpPages.size()) {
            sendMessage("This page does not exist");
            return;
        }

        sendMessage(helpPages.get(page));
    }
}

