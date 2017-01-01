package de.craftinc.gates.listeners;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.controllers.TeleportController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class VehicleMoveListener implements Listener {
    private TeleportController teleportController;

    public VehicleMoveListener(Plugin plugin) {
        this.teleportController = new TeleportController(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleMove(VehicleMoveEvent event) {
        Entity passenger = event.getVehicle().getPassenger();

        if (passenger instanceof Player) {
            teleportController.teleport((Player)passenger, event.getTo());
        }
    }
}
