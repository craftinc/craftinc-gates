package de.craftinc.gates.listeners;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.SimpleChunk;
import de.craftinc.gates.util.TeleportRequest;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ChunkLoadListener implements Listener
{
    private HashMap<SimpleChunk, List<TeleportRequest>> pendingRequests = new HashMap<SimpleChunk, List<TeleportRequest>>();
    private HashSet<Player> playersWithTeleportRequest = new HashSet<Player>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event)
    {
        System.out.println("loaded chunk: " + event.getChunk() + "world: " + event.getChunk().getWorld());
        System.out.println("pending: " + pendingRequests.keySet());

        SimpleChunk c = new SimpleChunk(event.getChunk());
        List<TeleportRequest> requests = pendingRequests.get(c);

        if (requests != null) {

            pendingRequests.remove(c);

            for (TeleportRequest r : requests) {

                Player p = r.getPlayer();
                this.playersWithTeleportRequest.remove(p);

                p.teleport(r.getDestination());
                p.sendMessage(ChatColor.DARK_AQUA + "Chunk got loaded.");
                p.sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
            }
        }
    }


    public void addTeleportRequest(final TeleportRequest r)
    {
        System.out.println("Got teleport request: " + r.getDestination().getChunk());

        if (r == null) {
            throw new IllegalArgumentException("The request must not be null!");
        }

        if (this.playersWithTeleportRequest.contains(r.getPlayer())) {
            return;
        }

        SimpleChunk c = new SimpleChunk(r.getDestination().getChunk());
        List<TeleportRequest> requests = pendingRequests.get(c);

        if (requests == null) {
            requests = new ArrayList<TeleportRequest>();
            this.pendingRequests.put(c, requests);
        }

        requests.add(r);
        this.playersWithTeleportRequest.add(r.getPlayer());
    }
}
