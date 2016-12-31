/*  Craft Inc. Gates
    Copyright (C) 2011-2013 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public abstract class BaseLocationCommand extends BaseCommand {

    protected Location getValidPlayerLocation() {
        // The player might stand in a half block or a sign or whatever
        // Therefore we load some extra locations and blocks
        Block playerBlock = player.getLocation().getBlock();
        Block upBlock = playerBlock.getRelative(BlockFace.UP);

        if (playerBlock.getType() == Material.AIR) {
            return player.getLocation();
        } else if (upBlock.getType() == Material.AIR) {
            return new Location(player.getLocation().getWorld(),
                    player.getLocation().getX(),
                    player.getLocation().getY() + 1,
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch());
        }

        return null;
    }
}
