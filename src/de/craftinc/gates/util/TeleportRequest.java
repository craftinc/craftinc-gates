package de.craftinc.gates.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportRequest
{
    private Player player;
    private Location destination;


    public TeleportRequest(Player player, Location destination)
    {
        if (player == null) {
            throw new IllegalArgumentException("Player must not be null!");
        }

        if (destination == null) {
            throw new IllegalArgumentException("Destination must not be null");
        }

        this.player = player;
        this.destination = destination;
    }


    public Player getPlayer()
    {
        return player;
    }


    public Location getDestination()
    {
        return destination;
    }
}
