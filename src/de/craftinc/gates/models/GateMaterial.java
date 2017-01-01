package de.craftinc.gates.models;

import org.bukkit.Material;

import java.security.InvalidParameterException;

public class GateMaterial {

    private Material material;

    public GateMaterial(String materialString) throws InvalidParameterException {
        Material material;

        switch (materialString) {
            case "sapling":
                material = Material.SAPLING;
                break;
            case "water":
                material = Material.STATIONARY_WATER;
                break;
            case "lava":
                material = Material.STATIONARY_LAVA;
                break;
            case "cobweb":
                material = Material.WEB;
                break;
            case "grass":
                material = Material.LONG_GRASS;
                break;
            case "dead bush":
                material = Material.DEAD_BUSH;
                break;
            case "dandelion":
                material = Material.YELLOW_FLOWER;
                break;
            case "poppy":
                material = Material.RED_ROSE;
                break;
            case "brown mushroom":
                material = Material.BROWN_MUSHROOM;
                break;
            case "red mushroom":
                material = Material.RED_MUSHROOM;
                break;
            case "torch":
                material = Material.TORCH;
                break;
            case "redstone torch (off)":
                material = Material.REDSTONE_TORCH_OFF;
                break;
            case "redstone torch (on)":
                material = Material.REDSTONE_TORCH_ON;
                break;
            case "fence":
                material = Material.FENCE;
                break;
            case "nether portal":
                material = Material.PORTAL;
                break;
            case "iron bars":
                material = Material.IRON_FENCE;
                break;
            case "glass pane":
                material = Material.THIN_GLASS;
                break;
            case "fence gate":
                material = Material.FENCE_GATE;
                break;
            case "nether brick fence":
                material = Material.NETHER_FENCE;
                break;
            case "nether wart":
                material = Material.NETHER_WARTS;
                break;
            case "end portal":
                material = Material.ENDER_PORTAL;
                break;
            case "cobblestone wall":
                material = Material.COBBLE_WALL;
                break;
            default:
                throw new InvalidParameterException();
        }

        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData(GateDirection direction) {
        switch (material) {
            case PORTAL:
                return direction == GateDirection.EastWest ? (byte)0b0 : (byte)0b10;
            case GRASS:
                return 1;
            default:
                return 0;
        }
    }
}