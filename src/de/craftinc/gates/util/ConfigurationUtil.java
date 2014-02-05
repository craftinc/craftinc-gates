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
package de.craftinc.gates.util;


import de.craftinc.gates.Plugin;
import org.bukkit.Material;

import java.util.logging.Level;


public class ConfigurationUtil
{
    public static final String confMaxGateBlocksKey = "maxGateBlocks";
    public static final String confPlayerGateBlockUpdateRadiusKey = "playerGateBlockUpdateRadius";
    public static final String confCheckForBrokenGateFramesKey = "checkForBrokenGateFrames";
    public static final String confGateTeleportMessageKey = "gateTeleportMessage";
    public static final String confGateTeleportVehicleNotAllowedMessageKey = "gateTeleportVehicleNotAllowedMessage";
    public static final String confShowTeleportMessageKey = "showTeleportMessage";
    public static final String confGateTeleportNoPermissionMessageKey = "gateTeleportNoPermissionMessage";
    public static final String confShowTeleportNoPermissionMessageKey = "showTeleportNoPermissionMessage";
    public static final String confSaveOnChangesKey = "saveOnChanges";
    public static final String confHighlightDurationKey = "highlightDuration";
    public static final String confGateMaterialKey = "gateMaterial";


    public static GateMaterial getPortalMaterial()
    {
        String materialString = Plugin.getPlugin().getConfig().getString(confGateMaterialKey);
        GateMaterial material = new GateMaterial();

        if (materialString.equals("sapling")) {
            material.material = Material.SAPLING;
        }
        else  if (materialString.equals("water")) {
            material.material = Material.STATIONARY_WATER;
        }
        else if (materialString.equals("lava")) {
            material.material = Material.STATIONARY_LAVA;
        }
        else if (materialString.equals("cobweb")) {
            material.material = Material.WEB;
        }
        else if (materialString.equals("grass")) {
            material.material = Material.LONG_GRASS;
            material.data = 1;
        }
        else if (materialString.equals("dead bush")) {
            material.material = Material.DEAD_BUSH;
        }
        else if (materialString.equals("dandelion")) {
            material.material = Material.YELLOW_FLOWER;
        }
        else if (materialString.equals("poppy")) {
            material.material = Material.RED_ROSE;
        }
        else if (materialString.equals("brown mushroom")) {
            material.material = Material.BROWN_MUSHROOM;
        }
        else if (materialString.equals("red mushroom")) {
            material.material = Material.RED_MUSHROOM;
        }
        else if (materialString.equals("torch")) {
            material.material = Material.TORCH;
        }
        else if (materialString.equals("redstone torch (off)")) {
            material.material = Material.REDSTONE_TORCH_OFF;
        }
        else if (materialString.equals("redstone torch (on)")) {
            material.material = Material.REDSTONE_TORCH_ON;
        }
        else if (materialString.equals("fence")) {
            material.material = Material.FENCE;
        }
        else if (materialString.equals("nether portal")) {
            material.material = Material.PORTAL;
        }
        else if (materialString.equals("iron bars")) {
            material.material = Material.IRON_FENCE;
        }
        else if (materialString.equals("glass pane")) {
            material.material = Material.THIN_GLASS;
        }
        else if (materialString.equals("fence gate")) {
            material.material = Material.FENCE_GATE;
        }
        else if (materialString.equals("nether brick fence")) {
            material.material = Material.NETHER_FENCE;
        }
        else  if (materialString.equals("nether wart")) {
            material.material = Material.NETHER_WARTS;
        }
        else if (materialString.equals("end portal")) {
            material.material = Material.ENDER_PORTAL;
        }
        else if (materialString.equals("cobblestone wall")) {
            material.material = Material.COBBLE_WALL;
        }
        else { // fallback!
            material.material = Material.PORTAL;
            Plugin.log(Level.WARNING, "Gate material invalid! Please check and correct your configuration file!");
        }

        return material;
    }
}


class GateMaterial
{
    public Material material = Material.PORTAL;
    public byte data = 0;
}
