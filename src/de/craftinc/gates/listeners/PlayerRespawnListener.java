package de.craftinc.gates.listeners;


import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;


public class PlayerRespawnListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        System.out.println("in onPlayerRespawn");
        System.out.println("player: " + event.getPlayer());
        System.out.println("position: " + event.getPlayer().getLocation());

        GateBlockChangeSender.updateGateBlocks(event.getPlayer(), event.getRespawnLocation());
    }
}
