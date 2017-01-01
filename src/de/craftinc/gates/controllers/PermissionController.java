package de.craftinc.gates.controllers;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.models.Gate;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class PermissionController {
    public static final String permissionInfo = "craftincgates.info";
    public static final String permissionManage = "craftincgates.manage";
    public static final String permissionUse = "craftincgates.use";

    private Permission permission;

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public boolean hasPermission(CommandSender sender, Gate gate, String permission) {
        assert(sender != null);
        assert(permission != null);

        final Location location = gate.getLocation();
        final Location exit = gate.getExit();

        boolean permAtLocation = location == null || hasPermission(sender, location.getWorld(), permission);
        boolean permAtExit = exit == null || hasPermission(sender, exit.getWorld(), permission);

        return permAtLocation && permAtExit;
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        return hasPermission(sender, (World)null, permission);
    }

    private boolean hasPermission(CommandSender sender, World world, String permission) {
        assert(sender != null);
        assert(permission != null);

        if (this.permission == null) {
            // fallback - use the standard bukkit permission system
            return sender.hasPermission(permission);
        }

        if (!(sender instanceof OfflinePlayer)) {
            return this.permission.has(sender, permission);
        }

        String worldName = world != null ? world.getName() : null;
        return this.permission.playerHas(worldName, (OfflinePlayer)sender, permission);
    }
}
