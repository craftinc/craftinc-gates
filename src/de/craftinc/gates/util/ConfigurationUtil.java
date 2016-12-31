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


public class ConfigurationUtil {
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


    static GateMaterial getPortalMaterial() {
        String materialString = Plugin.getPlugin().getConfig().getString(confGateMaterialKey);
        GateMaterial material = new GateMaterial();

        switch (materialString) {
            case "sapling":
                material.material = Material.SAPLING;
                break;
            case "water":
                material.material = Material.STATIONARY_WATER;
                break;
            case "lava":
                material.material = Material.STATIONARY_LAVA;
                break;
            case "cobweb":
                material.material = Material.WEB;
                break;
            case "grass":
                material.material = Material.LONG_GRASS;
                material.data = 1;
                break;
            case "dead bush":
                material.material = Material.DEAD_BUSH;
                break;
            case "dandelion":
                material.material = Material.YELLOW_FLOWER;
                break;
            case "poppy":
                material.material = Material.RED_ROSE;
                break;
            case "brown mushroom":
                material.material = Material.BROWN_MUSHROOM;
                break;
            case "red mushroom":
                material.material = Material.RED_MUSHROOM;
                break;
            case "torch":
                material.material = Material.TORCH;
                break;
            case "redstone torch (off)":
                material.material = Material.REDSTONE_TORCH_OFF;
                break;
            case "redstone torch (on)":
                material.material = Material.REDSTONE_TORCH_ON;
                break;
            case "fence":
                material.material = Material.FENCE;
                break;
            case "nether portal":
                material.material = Material.PORTAL;
                break;
            case "iron bars":
                material.material = Material.IRON_FENCE;
                break;
            case "glass pane":
                material.material = Material.THIN_GLASS;
                break;
            case "fence gate":
                material.material = Material.FENCE_GATE;
                break;
            case "nether brick fence":
                material.material = Material.NETHER_FENCE;
                break;
            case "nether wart":
                material.material = Material.NETHER_WARTS;
                break;
            case "end portal":
                material.material = Material.ENDER_PORTAL;
                break;
            case "cobblestone wall":
                material.material = Material.COBBLE_WALL;
                break;
            default:  // fallback!
                material.material = Material.PORTAL;
                Plugin.log(Level.WARNING, "Gate material invalid! Please check and correct your configuration file!");
                break;
        }

        return material;
    }
}


class GateMaterial {
    public Material material = Material.PORTAL;
    public byte data = 0;
}
