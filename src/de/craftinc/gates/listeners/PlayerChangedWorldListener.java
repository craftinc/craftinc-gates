package de.craftinc.gates.listeners;


import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;


public class PlayerChangedWorldListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event)
    {
        System.out.println("location: " + event.getPlayer().getLocation());
        GateBlockChangeSender.updateGateBlocks(event.getPlayer());
    }
}
