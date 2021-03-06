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
package de.craftinc.gates.models;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.ConfigurationUtil;
import de.craftinc.gates.util.FloodUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;


public class Gate implements ConfigurationSerializable {
    private Location location; /* saving both location and gateBlockLocations is redundant but makes it easy to allow players to reshape gates */
    private Set<Location> gateBlockLocations = new HashSet<>(); /* Locations of the blocks inside the gate */
    private Set<Block> gateFrameBlocks = new HashSet<>();
    private Location exit;
    private boolean isOpen = false;
    private boolean allowsVehicles = true;
    private String id;
    private GateMaterial material = new GateMaterial(Material.PORTAL);

    /**
     * You should never create two gates with the same 'id'. Also see 'setId(String id)'.
     *
     * @param id This parameter must not be 'null'. An exception will be thrown otherwise!
     */
    public Gate(final String id) {
        setId(id);
    }

    public String toString() {
        return super.toString() + " " + this.getId();
    }

    public GateDirection getDirection() {
        if (gateBlockLocations.isEmpty()) {
            return null;
        } else {
            Block some = ((Location)gateBlockLocations.toArray()[0]).getBlock();
            boolean east = gateBlockLocations.contains(some.getRelative(BlockFace.EAST).getLocation());
            boolean west = gateBlockLocations.contains(some.getRelative(BlockFace.WEST).getLocation());

            return east || west ? GateDirection.EastWest : GateDirection.NorthSouth;
        }
    }

    public GateMaterial getMaterial() {
        return material;
    }

    public void setMaterial(GateMaterial material) throws Exception {
        this.material = material;
        validate();
    }

    /**
     * @return This method might return a 'null' data.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location Supplying 'null' is permitted.
     * @throws Exception Will throw an exception if the gate is open and an invalid (no gate frame) location is
     *                   supplied. Note that the supplied 'location' will be set even if an exception is thrown. Note that this
     *                   gate will be closed if an exception is thrown.
     */
    public void setLocation(final Location location) throws Exception {
        this.location = location;

        if (isOpen) {
            findPortalBlocks();
            validate();
        } else {
            this.gateBlockLocations = new HashSet<>();
            this.gateFrameBlocks = new HashSet<>();
        }
    }

    /**
     * @return This method might return a 'null' value.
     */
    public Location getExit() {
        return exit;
    }

    /**
     * @param exit Supplying 'null' is permitted.
     * @throws Exception An exception will be thrown if 'null' data is supplied and this gate is open. Note that the
     *                   supplied 'exit' will be set even if an exception is thrown. Note that this gate will be closed if an
     *                   exception is thrown.
     */
    public void setExit(final Location exit) throws Exception {
        this.exit = exit;
        validate();
    }

    /**
     * @return This method will never return 'null'.
     */
    public String getId() {
        return id;
    }

    /**
     * Every gate should have an unique 'id'. You should therefore check if another gate with the same 'id' exists.
     * Note that this method will not check if another gate with the same 'id' exists!
     *
     * @param id This parameter must not be 'null'. An exception will be thrown otherwise!
     */
    public void setId(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("gate 'id' cannot be 'null'");
        }

        this.id = id.toLowerCase();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) throws Exception {
        if (isOpen && !this.isOpen) {
            findPortalBlocks();
        }

        this.isOpen = isOpen;
        validate();
    }

    public void setAllowsVehicles(boolean allowsVehicles) {
        this.allowsVehicles = allowsVehicles;
    }

    public boolean getAllowsVehicles() {
        return this.allowsVehicles;
    }

    /**
     * @return Will never return 'null' but might return an empty Set.
     */
    public Set<Location> getGateBlockLocations() {
        return gateBlockLocations;
    }

    /**
     * @return Will never return 'null' but might return an empty Set.
     */
    public Set<Block> getGateFrameBlocks() {
        return gateFrameBlocks;
    }

    private void findPortalBlocks() {
        gateBlockLocations = new HashSet<>();
        Set<Block> gateBlocks = FloodUtil.getGatePortalBlocks(location.getBlock());

        if (gateBlocks != null) {
            for (Block b : gateBlocks) {
                gateBlockLocations.add(b.getLocation());
            }
        }

        gateFrameBlocks = FloodUtil.getFrame(gateBlocks);
    }

    /**
     * Checks if values attributes do add up; will close gate on wrong values.
     */
    public void validate() throws Exception {
        if (!isOpen) {
            return;
        }

        if (location == null) {
            isOpen = false;
            this.gateBlockLocations = new HashSet<>();
            this.gateFrameBlocks = new HashSet<>();

            throw new Exception("Gate got closed. It has no location.");
        }

        if (exit == null) {
            isOpen = false;
            this.gateBlockLocations = new HashSet<>();
            this.gateFrameBlocks = new HashSet<>();

            throw new Exception("Gate got closed. It has no exit.");
        }

        if (gateBlockLocations.size() == 0) {
            isOpen = false;
            this.gateBlockLocations = new HashSet<>();
            this.gateFrameBlocks = new HashSet<>();

            throw new Exception("Gate got closed. The frame is missing or broken. (no gate blocks)");
        }

        validateFrame();
    }

    private void validateFrame() throws Exception {
        boolean isAir = material.getMaterial() == Material.AIR;
        boolean ignore = !Plugin.getPlugin().getConfig().getBoolean(ConfigurationUtil.confCheckForBrokenGateFramesKey);

        if (isAir || ignore) {
            return;
        }

        for (Block b : gateFrameBlocks) {

            if (b.getType() == Material.AIR) {
                isOpen = false;
                this.gateBlockLocations = new HashSet<>();
                this.gateFrameBlocks = new HashSet<>();

                throw new Exception("Gate got closed. The frame is missing or broken. (missing frame block(s))");
            }
        }
    }

    /*
     * INTERFACE: ConfigurationSerializable
     */
    static private String idKey = "id";
    static private String locationKey = "location";
    static private String gateBlocksKey = "gateBlocks";
    static private String exitKey = "exit";
    static private String materialKey = "material";
    static private String isOpenKey = "open";
    static private String allowsVehiclesKey = "allowsVehiclesKey";

    @SuppressWarnings("unchecked")
    public Gate(Map<String, Object> map) {
        try {
            id = map.get(idKey).toString().toLowerCase();
            location = (Location) map.get(locationKey);
            exit = (Location) map.get(exitKey);
            material = new GateMaterial((String)map.get(materialKey));
            isOpen = (Boolean) map.get(isOpenKey);
            allowsVehicles = (Boolean) map.get(allowsVehiclesKey);
            gateBlockLocations = (Set<Location>) map.get(gateBlocksKey);

            gateFrameBlocks = FloodUtil.getFrameWithLocations(gateBlockLocations);
        } catch (Exception e) {
            Plugin.log("ERROR: Failed to load gate '" + id + "'! (" + e.getMessage() + ")");
            Plugin.log("NOTE:  This gate will be removed from 'gates.yml' and added to 'invalid_gates.yml'!");

            Plugin.getPlugin().getGatesManager().storeInvalidGate(map);
        }

        try {
            validate(); // make sure to not write invalid stuff to disk
        } catch (Exception e) {
            Plugin.log("The loaded gate " + this.getId() + " seems to be not valid: " + e.getMessage());
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> retVal = new HashMap<>();

        retVal.put(idKey, id);
        retVal.put(isOpenKey, isOpen);
        retVal.put(allowsVehiclesKey, allowsVehicles);
        retVal.put(gateBlocksKey, gateBlockLocations);
        retVal.put(materialKey, material.toString());

        if (exit != null) {
            retVal.put(exitKey, exit);
        }

        if (location != null) {
            retVal.put(locationKey, location);
        }

        return retVal;
    }
}
