package de.craftinc.gates.listeners;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import de.craftinc.gates.Gate;
import de.craftinc.gates.util.GateUtil;


public class PluginPortalListener implements Listener
{
	private HashMap<Player, Gate> currentGateAtEvent = new HashMap<Player, Gate>();
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPortal(PlayerPortalEvent event)
	{		
		if (event.isCancelled())
		{
			return;
		}

		Location playerLocation = event.getPlayer().getLocation();
		Gate gateAtLocation = GateUtil.getGateAtPlayerLocation(playerLocation);
		
		
		// If the player's gamemode is creative no gate might be found!
		// It seems like players get teleported on a move event when the 'to' location is
		// inside a gate. This meens the location obtained earlier is NOT inside a gate.
		if (gateAtLocation == null && event.getPlayer().getGameMode() == GameMode.CREATIVE) 
		{
			gateAtLocation = this.currentGateAtEvent.get(event.getPlayer());
		}
		
		if (gateAtLocation != null) 
		{
			event.setCancelled(true);
		}
		
		
		this.currentGateAtEvent.put(event.getPlayer(), null);
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityPortalEnterEvent(EntityPortalEnterEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			
			if (player.getGameMode() == GameMode.CREATIVE)
			{
				if (this.currentGateAtEvent.get(player) != null) 
				{
					return;
				}
				
				Location eventLocation = event.getLocation();
				Gate closestGate = GateUtil.closestGate(eventLocation);
				
				if (closestGate != null) 
				{
					// Make sure gate and event locations are on the same height (y-value).
					// Otherwise the distance will be messed up when players are flying.
					// FIX ME: this could potentially let a nearby nether portal fail!
					eventLocation.setY(closestGate.getLocation().getY());
					
					double distToClosestGate = closestGate.getLocation().distance(eventLocation);

					if (distToClosestGate < 2.0) {
						this.currentGateAtEvent.put(player, closestGate);
						return;
					}
				}
			}
			
			this.currentGateAtEvent.put(player, null);
		}
	}

}
