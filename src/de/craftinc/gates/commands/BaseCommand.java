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

import de.craftinc.gates.util.ConfigurationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

public abstract class BaseCommand {

    protected List<String> aliases = new ArrayList<>();
    protected List<String> requiredParameters = new ArrayList<>();
    List<String> optionalParameters = new ArrayList<>();

    protected String helpDescription = "no description";

    List<String> parameters;
    CommandSender sender;
    protected Player player;
    protected Gate gate;

    protected boolean senderMustBePlayer = true;
    boolean hasGateParam = true;

    protected String requiredPermission;
    protected boolean needsPermissionAtCurrentLocation;

    protected boolean shouldPersistToDisk;

    public List<String> getAliases() {
        return aliases;
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

    private boolean getSaveOnChanges() {
        FileConfiguration config = Plugin.getPlugin().getConfig();
        return config.getBoolean(ConfigurationUtil.confSaveOnChangesKey);
    }

    abstract protected void perform();

    protected void sendMessage(String message) {
        sender.sendMessage(message);
    }

    protected void sendMessage(List<String> messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
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
                        this.getUsageTemplate()
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
        if (Plugin.getPermission() == null) { // fallback - use the standard bukkit permission system
            return this.sender.hasPermission(this.requiredPermission);
        }

        if (!(this.sender instanceof Player)) {
            // sender is no player - there is no information about the senders locations
            return Plugin.getPermission().has(this.sender, this.requiredPermission);
        }


        Player p = (Player) this.sender;
        boolean hasPermission = false;

        switch (this.requiredPermission) {
            case Plugin.permissionInfo:

                if (this.hasGateParam) {
                    hasPermission = this.hasPermissionAtGateLocationAndExit(p);
                } else {
                    hasPermission = hasPermissionAtPlayerLocation(p);
                }
                break;
            case Plugin.permissionUse:
                hasPermission = this.hasPermissionAtGateLocationAndExit(p);
                break;
            case Plugin.permissionManage:

                if (this.needsPermissionAtCurrentLocation && this.hasGateParam) {
                    boolean hasPermissionAtCurrentLocation = hasPermissionAtPlayerLocation(p);
                    hasPermission = hasPermissionAtCurrentLocation && this.hasPermissionAtGateLocationAndExit(p);
                } else if (this.needsPermissionAtCurrentLocation) {
                    hasPermission = hasPermissionAtPlayerLocation(p);
                } else {
                    hasPermission = this.hasPermissionAtGateLocationAndExit(p);
                }
                break;
        }

        return hasPermission;
    }

    private boolean hasPermissionAtGateLocationAndExit(Player p) {
        if (this.gate == null || p == null) {
            return false;
        }

        boolean permAtLocation = this.gate.getLocation() == null || Plugin.getPermission().has(this.gate.getLocation().getWorld(), p.getName(), this.requiredPermission);
        boolean permAtExit = this.gate.getExit() == null || Plugin.getPermission().has(this.gate.getExit().getWorld(), p.getName(), this.requiredPermission);

        return permAtLocation & permAtExit;
    }

    private boolean hasPermissionAtPlayerLocation(Player p) {
        return Plugin.getPermission().has(p.getWorld(), p.getName(), this.requiredPermission);
    }

    /*
        Help and usage description
    */

    String getUsageTemplate(boolean withDescription) {
        String ret = "";

        ret += ChatColor.AQUA;
        ret += "/" + Plugin.getPlugin().getBaseCommand() + " " + TextUtil.implode(this.getAliases(), ",") + " ";

        List<String> parts = new ArrayList<>();

        for (String requiredParameter : this.requiredParameters) {
            parts.add("[" + requiredParameter + "]");
        }

        for (String optionalParameter : this.optionalParameters) {
            parts.add("*[" + optionalParameter + "]");
        }


        ret += ChatColor.DARK_AQUA;
        ret += TextUtil.implode(parts, " ");

        if (withDescription) {
            ret += " ";
            ret += ChatColor.YELLOW;
            ret += this.helpDescription;
        }
        return ret;
    }

    private String getUsageTemplate() {
        return getUsageTemplate(false);
    }
}
