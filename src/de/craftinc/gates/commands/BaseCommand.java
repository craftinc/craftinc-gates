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
import java.util.List;

import de.craftinc.gates.controllers.PermissionController;
import de.craftinc.gates.util.ConfigurationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.craftinc.gates.models.Gate;
import de.craftinc.gates.controllers.GatesManager;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

public abstract class BaseCommand {
    PermissionController permissionController;

    List<String> aliases = new ArrayList<>();
    List<String> requiredParameters = new ArrayList<>();
    List<String> optionalParameters = new ArrayList<>();

    String helpDescription = "no description";

    List<String> parameters;
    CommandSender sender;
    Player player;
    Gate gate;

    boolean senderMustBePlayer = true;
    boolean hasGateParam = true;

    String requiredPermission;
    boolean needsPermissionAtCurrentLocation;

    boolean shouldPersistToDisk;

    public List<String> getAliases() {
        return aliases;
    }

    public BaseCommand() {
        permissionController = Plugin.getPlugin().getPermissionController();
    }

    public void execute(CommandSender sender, List<String> parameters) {
        this.sender = sender;
        this.parameters = parameters;

        if (!this.validateCall()) {
            return;
        }

        if (this.senderMustBePlayer) {
            this.player = (Player)sender;
        }

        this.perform();

        if (this.shouldPersistToDisk && getSaveOnChanges()) {
            Plugin.getPlugin().getGatesManager().saveGatesToDisk();
        }
    }

    abstract protected void perform();

    void sendMessage(String message) {
        sender.sendMessage(message);
    }

    void sendMessage(List<String> messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    boolean setGateUsingParameter(String param) {
        GatesManager gateManager = Plugin.getPlugin().getGatesManager();

        if (!gateManager.gateExists(param)) {
            return false;
        } else {
            gate = gateManager.getGateWithId(param);
            return true;
        }
    }

    /**
     * This will return false if a gate is required for this command but this.gate == null.
     */
    boolean hasPermission() {
        if (needsPermissionAtCurrentLocation) {
            return permissionController.hasPermission(sender, requiredPermission);
        } else {
            return permissionController.hasPermission(sender, gate, requiredPermission);
        }
    }

    /*
        Help and usage description
    */

    String getUsageTemplate(boolean withDescription) {
        String ret = "";

        ret += ChatColor.AQUA;
        ret += "/" + Plugin.getPlugin().getBaseCommand() + " " + TextUtil.implode(getAliases(), ",") + " ";

        List<String> parts = new ArrayList<>();

        for (String requiredParameter : requiredParameters) {
            parts.add("[" + requiredParameter + "]");
        }

        for (String optionalParameter : optionalParameters) {
            parts.add("*[" + optionalParameter + "]");
        }


        ret += ChatColor.DARK_AQUA;
        ret += TextUtil.implode(parts, " ");

        if (withDescription) {
            ret += " ";
            ret += ChatColor.YELLOW;
            ret += helpDescription;
        }
        return ret;
    }

    private boolean validateCall() {
        boolean allParametersThere = parameters.size() >= requiredParameters.size();
        boolean senderIsPlayer = this.sender instanceof Player;
        boolean hasGateParameter = false;

        if (this.hasGateParam && this.parameters.size() > 0 && this.setGateUsingParameter(this.parameters.get(0))) {
            hasGateParameter = true;
        }

        boolean senderHasPermission = this.hasPermission();
        boolean valid;

        if (this.senderMustBePlayer && !senderIsPlayer) {
            sendMessage(ChatColor.RED + "This command can only be used by in-game players.");
            valid = false;
        } else {
            if (!allParametersThere) {
                sendMessage(ChatColor.RED + "Some parameters are missing! " +
                        ChatColor.AQUA + "Usage: " +
                        getUsageTemplate(false)
                );
                valid = false;
            } else if ((!senderHasPermission && this.hasGateParam) ||
                    (!senderHasPermission) ||
                    (this.hasGateParam && !hasGateParameter)) {

                sendMessage(ChatColor.RED +
                        "You either provided a invalid gate or do not have permission to " +
                        this.helpDescription.toLowerCase()
                );
                valid = false;
            } else {
                valid = true;
            }
        }

        return valid;
    }

    private boolean getSaveOnChanges() {
        FileConfiguration config = Plugin.getPlugin().getConfig();
        return config.getBoolean(ConfigurationUtil.confSaveOnChangesKey);
    }
}
