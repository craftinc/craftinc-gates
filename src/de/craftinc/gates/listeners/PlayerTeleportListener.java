package de.craftinc.gates.listeners;


import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled()) {
            return;
        }

        GateBlockChangeSender.updateGateBlocks(event.getPlayer(), event.getTo());
    }
}
