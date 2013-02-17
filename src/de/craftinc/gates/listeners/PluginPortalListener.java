package de.craftinc.gates.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.GateUtil;

public class PluginPortalListener implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPortal(PlayerPortalEvent event)
	{		
		if (event.isCancelled())
		{
			Plugin.log("event has already been cancelled");
			return;
		}
		
		Location playerLocation = event.getPlayer().getLocation();
		
		// Find the gate at the current location.
		Gate gateAtLocation = GateUtil.getGateAtPlayerLocation(playerLocation);
		
		
		// If the player's gamemode is creative no gate might be found!
		// It seems like players get teleported on a move event when the 'to' location is
		// inside a gate. This meens the location obtained earlier is NOT inside a gate.
		// 
		if (gateAtLocation == null && event.getPlayer().getGameMode() == GameMode.CREATIVE) 
		{
			Gate closestGate = GateUtil.closestGate(playerLocation);
			
			if (closestGate != null) 
			{	
				// Make sure gate and player locations are on the same height (y-value).
				// Otherwise the distance will be messed up when players are flying.
				// FIX ME: this could potentially let a nearby nether portal fail!
				playerLocation.setY(closestGate.getLocation().getY());
				double distToClosestGate = closestGate.getLocation().distance(playerLocation);
				
				if (distToClosestGate <= 5.0) // the player location is often not very accurate
				{
					gateAtLocation = closestGate;
				}
			}
		}
		
		if (gateAtLocation != null) 
		{
			event.setCancelled(true);
		}
	}

}
