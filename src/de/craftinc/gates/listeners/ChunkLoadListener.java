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

import java.util.HashMap;


public class ChunkLoadListener implements Listener
{
    private HashMap<Chunk, TeleportRequest> pendingRequests = new HashMap<Chunk, TeleportRequest>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkLoadEvent event)
    {
        Chunk c = event.getChunk();
        TeleportRequest request = pendingRequests.get(c);

        if (request != null) {

            pendingRequests.remove(c);

            Player p = request.getPlayer();
            p.teleport(request.getDestination());
            p.sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
        }
    }


    public void addTeleportRequest(TeleportRequest request)
    {
        if (request == null) {
            throw new IllegalArgumentException("The request must not be null!");
        }

        this.pendingRequests.put(request.getDestination().getChunk(), request);
    }
}
