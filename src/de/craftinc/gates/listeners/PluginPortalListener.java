package de.craftinc.gates.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import de.craftinc.gates.Gate;

public class PluginPortalListener extends BaseLocationListener implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPortal(PlayerPortalEvent event)
	{		
		if (event.isCancelled()) {
			return;
		}
		
		// Find the gate at the current location.
		Gate gateAtLocation = getGateAtPlayerLocation(event);
		
		if (gateAtLocation != null) {
			event.setCancelled(true);
		}
	}

}
