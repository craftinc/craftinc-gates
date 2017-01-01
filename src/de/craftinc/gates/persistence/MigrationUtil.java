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
package de.craftinc.gates.persistence;

import de.craftinc.gates.models.Gate;

import de.craftinc.gates.Plugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.logging.Level;


public class MigrationUtil {

    public static boolean performMigration(int storageVersion, int currentVersion, List<Gate> gates) {
        if (storageVersion != currentVersion) {
            Plugin.log(Level.SEVERE, "Supplied storage version is currently not supported!" +
                    "Make sure you have the latest version of Craft Inc. Gates installed. Plugin will be disabled!");
            return false;
        }
        return true;
    }
}
