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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

public class CommandList extends BaseCommand {

    private static final int linesPerPage = 10;

    /* this is actually not true. the font used by Minecraft is not
       monospaced. but there seems to be no (easy) way to calculate
       the drawing-size of a string.
     */
    private static final int charactersPerLine = 52;

    public CommandList() {
        aliases.add("list");
        aliases.add("ls");

        optionalParameters.add("page");
        hasGateParam = false;
        needsPermissionAtCurrentLocation = false;

        helpDescription = "lists all availible gates.";

        requiredPermission = Plugin.permissionInfo;
        shouldPersistToDisk = false;
        senderMustBePlayer = false;
    }

    public void perform() {
        int page = this.getPageParameter();
        List<String> allPages = this.pagedGateIds();

        if (allPages == null) {    // no gates exist
            sendMessage(ChatColor.RED + "There are no gates yet. " + ChatColor.RESET +
                    "(Note that you might not be allowed to get information about certain gates)");
            return;
        }

        if (page > allPages.size() || page < 1) {
            sendMessage(ChatColor.RED + "The requested page is not availible");
            return;
        }

        String message = TextUtil.titleSize("List of all gates (" + page + "/" + allPages.size() + ")") + "\n";
        message += allPages.get(page - 1);

        sendMessage(message);
    }

    private static List<String> linesOfGateIds(List<String> gates) {
        List<String> lines = new ArrayList<>();

        int index = 0;
        List<String> gateIdsForCurrentLine = new ArrayList<>();
        int numCharactersInCurrentLine = 0;

        while (index < gates.size()) {
            String gateId = gates.get(index);
            int gateIdLength = gateId.length() + 2; // actual length + comma + whitespace

            if (gateIdLength > charactersPerLine && numCharactersInCurrentLine == 0) { // special case: very long gate id
                gateIdsForCurrentLine = new ArrayList<>();
                numCharactersInCurrentLine = 0;

                while ((gateId.length() + 2) > charactersPerLine) {
                    int cutPos = charactersPerLine;

                    // is the id too long to add comma and whitespace but not longer than the line?
                    if (gateId.length() <= charactersPerLine) {
                        cutPos -= 2;
                    }

                    lines.add(gateId.substring(0, cutPos));
                    gateId = gateId.substring(cutPos, gateId.length());
                }

                gateIdsForCurrentLine.add(gateId);

                numCharactersInCurrentLine += gateId.length();
                index++;
            } else if ((numCharactersInCurrentLine + gateIdLength) <= charactersPerLine) { // gate fits into current line
                gateIdsForCurrentLine.add(gateId);
                numCharactersInCurrentLine += gateIdLength;

                index++;
            } else {  // the current gate does not fit on the
                lines.add(TextUtil.implode(gateIdsForCurrentLine, ", ") + ", ");

                gateIdsForCurrentLine = new ArrayList<>();
                numCharactersInCurrentLine = 0;
            }
        }

        lines.add(TextUtil.implode(gateIdsForCurrentLine, ", "));
        return lines;
    }

    private static String intToTitleString(int i, boolean addPreviousPageNote, boolean addNextPageNote) {
        String retVal = ChatColor.DARK_AQUA + "";

        if (i < 26) {
            retVal += (char) (i + 65);
        } else if (i == 26) {
            retVal += "0-9";
        } else {
            retVal += "!@#$";
        }

        if (addPreviousPageNote && addNextPageNote) {
            retVal += " (more on previous and next page)";
        } else if (addPreviousPageNote) {
            retVal += " (more on previous page)";
        } else if (addNextPageNote) {
            retVal += " (more on next page)";
        }

        return retVal + "\n";
    }

    /**
     * Method for getting a collection of gates the player is allowed to see.
     */
    private Collection<Gate> getAllGates() {
        Collection<Gate> gates = Plugin.getPlugin().getGatesManager().allGates();

        if (this.sender instanceof Player && Plugin.getPermission() != null) {
            Player p = (Player) this.sender;

            // create a copy since we cannot iterate over a collection while modifying it!
            Collection<Gate> gatesCopy = new ArrayList<>(gates);

            for (Gate gate : gatesCopy) {
                if (gate.getLocation() != null) {
                    boolean permissionAtGateLocation = Plugin.getPermission().has(gate.getLocation().getWorld(), p.getName(), this.requiredPermission);
                    if (!permissionAtGateLocation) {
                        gates.remove(gate);
                        continue;
                    }
                }

                if (gate.getExit() != null) {
                    boolean permissionAtGateExit = Plugin.getPermission().has(gate.getExit().getWorld(), p.getName(), this.requiredPermission);
                    if (!permissionAtGateExit) {
                        gates.remove(gate);
                    }
                }
            }
        }

        return gates;
    }

    /**
     * Sorts all gates by there first character.
     * Puts gates in corresponding Lists: (all returned lists will be sorted alphabetically)
     * list 0-25: a,b,c,..,z
     * list 26: 0-9
     * list 27: other
     */
    private static List<List<String>> gatesSortedByName(Collection<Gate> allGates) {
        // create the lists
        List<List<String>> ids = new ArrayList<>();

        for (int i = 0; i < 28; i++) {
            ids.add(new ArrayList<String>());
        }

        // put all gates into correct lists
        for (Gate gate : allGates) {
            String id = gate.getId();
            int first = id.charAt(0);

            if (first > 96 && first < 123) { // convert lower case chars
                first -= 97;
            } else if (first > 64 && first < 91) { // convert upper case chars
                first -= 65;
            } else if (first > 47 && first < 58) { // convert numbers
                first = 26;
            } else { // everything else
                first = 27;
            }

            ids.get(first).add(id);
        }

        // sort everything
        for (int i = 0; i < 28; i++) {
            Collections.sort(ids.get(i));
        }

        return ids;
    }

    /**
     * Returns a list of strings.
     * Each string is the text for a page.
     * The maximum number of lines per page is 'linesPerPage' minus 1.
     * Will return an empty list if no gates are availible.
     */
    private List<String> pagedGateIds() {
        Collection<Gate> gates = this.getAllGates();

        if (gates.size() == 0) {
            return null;
        }

        List<List<String>> gatesSortedByName = gatesSortedByName(gates);
        List<String> allPages = new ArrayList<>();
        int linesLeftOnPage = linesPerPage - 1;
        String currentPageString = "";

        for (int i = 0; i < gatesSortedByName.size(); i++) {

            List<String> currentGates = gatesSortedByName.get(i);

            if (currentGates.isEmpty()) {
                continue;
            }

            List<String> currentGatesAsLines = linesOfGateIds(currentGates);
            boolean moreGatesOnLastPage = false;

            while (!currentGatesAsLines.isEmpty()) {

                if (linesLeftOnPage < 2) {
                    currentPageString = currentPageString.substring(0, currentPageString.length() - 2); // remove newlines add the end of the page
                    allPages.add(currentPageString);
                    currentPageString = "";

                    linesLeftOnPage = linesPerPage - 1;
                }

                // calculate number of lines to add to current page
                int linesNecessaryForCurrentGates = currentGatesAsLines.size();
                int linesToFill;
                boolean moreGatesOnNextPage;

                if (linesNecessaryForCurrentGates < linesLeftOnPage) {
                    linesToFill = linesNecessaryForCurrentGates;
                    moreGatesOnNextPage = false;
                } else {
                    linesToFill = linesLeftOnPage - 1;
                    moreGatesOnNextPage = true;
                }

                // add title
                currentPageString += intToTitleString(i, moreGatesOnLastPage, moreGatesOnNextPage);
                currentPageString += ChatColor.AQUA;
                linesLeftOnPage--;

                // add gate lines
                for (int j = 0; j < linesToFill; j++) {
                    currentPageString += currentGatesAsLines.get(j) + "\n";
                }

                // remove lines added
                for (int j = 0; j < linesToFill; j++) {
                    currentGatesAsLines.remove(0);
                }

                // cleanup
                moreGatesOnLastPage = linesNecessaryForCurrentGates >= linesLeftOnPage;
                linesLeftOnPage -= linesToFill;
            }
        }

        // add the last page
        if (!currentPageString.isEmpty()) {
            currentPageString = currentPageString.substring(0, currentPageString.length() - 2); // remove newlines add the end of the page
            allPages.add(currentPageString);
        }

        return allPages;
    }


    private int getPageParameter() {
        int page = 1;

        try {
            page = new Integer(parameters.get(0));
        } catch (Exception ignored) {
        }

        return page;
    }
}
