package de.craftinc.gates.controllers;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.models.Gate;
import de.craftinc.gates.util.ConfigurationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Calendar;
import java.util.HashMap;

public class TeleportController {
    private TeleportMessageUtil messageUtil;
    private GatesManager gatesManager;
    private PermissionController permissionController;

    public TeleportController(Plugin plugin) {
        this.gatesManager = plugin.getGatesManager();
        this.messageUtil = new TeleportMessageUtil(plugin.getConfig());
        this.permissionController = plugin.getPermissionController();
    }

    /**
     * Try teleporting a player at given location.
     */
    public void teleport(final Player player, Location toLocation) {
        final Gate gateAtLocation = gatesManager.getGateAtLocation(toLocation);

        if ((gateAtLocation == null) || !gateAtLocation.isOpen()) {
            return;
        }

        if (!permissionController.hasPermission(player, gateAtLocation, PermissionController.permissionUse)) {
            messageUtil.sendNoPermissionMessage(player);
            return;
        }

        this.teleportPlayer(player, gateAtLocation);
    }

    private void teleportPlayer(final Player player, final Gate gate) {
        final Entity vehicle = player.getVehicle();

        if (vehicle != null && !gate.getAllowsVehicles()) {
            messageUtil.sendVehicleForbiddenMessage(player);
            return;
        }

        final Location destination = calculateDestination(player, gate);

        if (vehicle != null) {
            vehicle.teleport(destination, PlayerTeleportEvent.TeleportCause.PLUGIN);
            vehicle.setPassenger(player);
        } else {
            player.teleport(destination);
        }

        messageUtil.sendTeleportMessage(player);
    }

    private Location calculateDestination(Player player, Gate gate) {
        final Location exit = gate.getExit();
        final Location pLocation = player.getLocation();
        final Float newYaw = exit.getYaw() - gate.getLocation().getYaw() + pLocation.getYaw();

        return new Location(exit.getWorld(),
                exit.getX(),
                exit.getY(),
                exit.getZ(),
                newYaw,
                pLocation.getPitch()
        );
    }
}

class TeleportMessageUtil {
    private HashMap<String, Long> lastNoPermissionMessages = new HashMap<>();
    private FileConfiguration config;

    TeleportMessageUtil(FileConfiguration config) {
        this.config = config;
    }

    void sendVehicleForbiddenMessage(Player player) {
        if (!config.getBoolean(ConfigurationUtil.confShowTeleportNoPermissionMessageKey)) {
            return;
        }

        final String notAllowedMessage = config.getString(ConfigurationUtil.confGateTeleportVehicleNotAllowedMessageKey);
        player.sendMessage(ChatColor.DARK_AQUA + notAllowedMessage);
    }

    void sendNoPermissionMessage(Player player) {
        if (!config.getBoolean(ConfigurationUtil.confShowTeleportNoPermissionMessageKey)) {
            return;
        }
        final String playerName = player.getPlayer().getName();

        if (playerName == null) {
            return;
        }

        final Long now = Calendar.getInstance().getTimeInMillis();

        // do not display messages more often than once per second
        if (!this.lastNoPermissionMessages.containsKey(playerName)
                || this.lastNoPermissionMessages.get(playerName) < now - 10000L) {

            final String noPermissionString = config.getString(ConfigurationUtil.confGateTeleportNoPermissionMessageKey);
            player.sendMessage(ChatColor.RED + noPermissionString);
            this.lastNoPermissionMessages.put(playerName, now);
        }
    }

    void sendTeleportMessage(Player player) {
        if (!config.getBoolean(ConfigurationUtil.confShowTeleportMessageKey)) {
            return;
        }
        final String teleportMessage = config.getString(ConfigurationUtil.confGateTeleportMessageKey);
        player.sendMessage(ChatColor.DARK_AQUA + teleportMessage);
    }
}