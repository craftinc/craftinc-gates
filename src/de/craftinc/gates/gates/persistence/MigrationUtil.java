package de.craftinc.gates.gates.persistence;

import de.craftinc.gates.gates.Gate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;


public class MigrationUtil
{
    public static void performMigration(int storageVersion, int currentVersion, List<Gate> gates)
    {
        if (currentVersion == 1 && storageVersion == 0) {

            for (Gate g : gates) {

                for (Location l : g.getGateBlockLocations()) {
                    Block b = l.getBlock();

                    if (b.getType() == Material.PORTAL) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }
        else {
            throw new IllegalArgumentException("Supplied storage version is currently not supported! Make sure you have the latest version of Craft Inc. Gates installed.");
        }
    }
}
