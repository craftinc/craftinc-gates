package de.craftinc.gates.listeners;

import de.craftinc.gates.Plugin;
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
import java.util.List;


public class ChunkLoadListener implements Listener
{
    private HashMap<Chunk, List<TeleportRequest>> pendingRequests = new HashMap<Chunk, List<TeleportRequest>>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event)
    {
        Chunk c = event.getChunk();
        List<TeleportRequest> requests = pendingRequests.get(c);

        if (requests != null) {

            pendingRequests.remove(c);

            for (TeleportRequest r : requests) {

                Player p = r.getPlayer();


                p.sendMessage(ChatColor.DARK_AQUA + "Chunk got loaded.");


                p.teleport(r.getDestination());
                p.sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
            }
        }
    }


    public void addTeleportRequest(final TeleportRequest r)
    {
        if (r == null) {
            throw new IllegalArgumentException("The request must not be null!");
        }

        Chunk c = r.getDestination().getChunk();
        List<TeleportRequest> requests = pendingRequests.get(c);

        if (requests == null) {
            requests = new ArrayList<TeleportRequest>();
            this.pendingRequests.put(c, requests);
        }

        requests.add(r);
    }
}
