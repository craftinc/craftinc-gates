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
package de.craftinc.gates;

import de.craftinc.gates.util.FloodUtil;
import de.craftinc.gates.persistence.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;


public class Gate implements ConfigurationSerializable
{
	protected Location location; /* saving both location and gateBlockLocations is redundant but makes it easy to allow players to reshape gates */
	protected Set<Location> gateBlockLocations = new HashSet<Location>(); /* Locations of the blocks inside the gate */
    protected Set<Block> gateFrameBlocks = new HashSet<Block>();
	
	protected Location exit;
	
	protected boolean isHidden = false;
	protected boolean isOpen = false;
	
	protected String id;

    /**
     * You should never create two gates with the same 'id'. Also see 'setId(String id)'.
     * @param id This parameter must not be 'null'. An exception will be thrown otherwise!
     */
	public Gate(final String id)
	{
        setId(id.toLowerCase());
	}
	
	
	public String toString()
	{
		return super.toString() + " " + this.getId();
	}
	
	
	/*
	 * SETTER & GETTER
	 */

    /**
     *
     * @return This method might return a 'null' value.
     */
	public Location getLocation() 
	{
		return location;
	}


    /**
     *
     * @param location Supplying 'null' is permitted.
     * @throws Exception Will throw an exception if the gate is open and an invalid (no gate frame) location is
     *         supplied. Note that the supplied 'location' will be set even if an exception is thrown. Note that this
     *         gate will be closed if an exception is thrown.
     */
	public void setLocation(Location location)  throws Exception
	{
		this.location = location;
		
		if (isOpen) {
            if (this.gateBlockLocations == null || this.gateBlockLocations.size() == 0 ) {
                findPortalBlocks();
            }

			validate();
		}
	}


    /**
     *
     * @return This method might return a 'null' value.
     */
	public Location getExit() 
	{
		return exit;
	}


    /**
     *
     * @param exit Supplying 'null' is permitted.
     * @throws Exception An exception will be thrown if 'null' value is supplied and this gate is open. Note that the
     *         supplied 'exit' will be set even if an exception is thrown. Note that this gate will be closed if an
     *         exception is thrown.
     */
	public void setExit(Location exit) throws Exception
	{
		this.exit = exit;
		validate();
	}


    /**
     *
     * @return This method will never return 'null'.
     */
	public String getId()
    {
		return id;
	}


    /**
     * Every gate should have an unique 'id'. You should therefore check if another gate with the same 'id' exists.
     * Note that this method will not check if another gate with the same 'id' exists!
     * @param id This parameter must not be 'null'. An exception will be thrown otherwise!
     */
	public void setId(String id)
    {
        if (id == null) {
            throw new IllegalArgumentException("gate 'id' cannot be 'null'");
        }

		this.id = id;
	}


	public boolean isHidden() 
	{
		return isHidden;
	}
	
	
	public void setHidden(boolean isHidden) throws Exception
	{
		this.isHidden = isHidden;
        this.validate();
	}
	
	
	public boolean isOpen() 
	{
		return isOpen;
	}
	
	
	public void setOpen(boolean isOpen) throws Exception
	{
		if (isOpen && !this.isOpen) {
            findPortalBlocks();
		}

		this.isOpen = isOpen;
		validate();
	}


    /**
     *
     * @return Will never return 'null' but might return an empty Set.
     */
	public Set<Location> getGateBlockLocations() 
	{
		return gateBlockLocations;
	}


    /**
     *
     * @return Will never return 'null' but might return an empty Set.
     */
    public Set<Block> getGateFrameBlocks()
    {
        return gateFrameBlocks;
    }



	protected void findPortalBlocks()
	{
		gateBlockLocations = new HashSet<Location>();
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
	public void validate() throws Exception
	{
		if (!isOpen) {
			return;
		}
		
		if (location == null) {
			setOpen(false);
			throw new Exception("Gate got closed. It has no location.");
		}
		
		if (exit == null) {
			setOpen(false);
			throw new Exception("Gate got closed. It has no exit.");
		}
		
		if (gateBlockLocations.size() == 0) {
			setOpen(false);
			throw new Exception("Gate got closed. The frame is missing or broken. (no gate blocks)");
		}

        if (!isHidden() && Plugin.getPlugin().getConfig().getBoolean(Plugin.confCheckForBrokenGateFramesKey)) {

            for (Block b : gateFrameBlocks) {

                if (b.getType() == Material.AIR) {
                    setOpen(false);
                    throw new Exception("Gate got closed. The frame is missing or broken. (missing frame block(s))");
                }
            }
        }
	}


	/*
	 * INTERFACE: ConfigurationSerializable
	 */
	static protected String idKey = "id";
	static protected String locationKey = "location";
	static protected String gateBlocksKey = "gateBlocks";
	static protected String exitKey = "exit";
	static protected String isHiddenKey = "hidden";
	static protected String isOpenKey = "open";
	static protected String locationYawKey = "locationYaw";
	static protected String locationPitchKey = "locationPitch";
	static protected String exitYawKey = "exitYaw";
	static protected String exitPitchKey = "exitPitch";
	
	
	@SuppressWarnings("unchecked")
    public Gate(Map<String, Object> map)
	{
        try {
			id = map.get(idKey).toString();

            if (id == null) {
                throw new Exception("gates need to have an id");
            }

			isHidden = (Boolean)map.get(isHiddenKey);
			isOpen = (Boolean)map.get(isOpenKey);
			
			location = LocationUtil.deserializeLocation((Map<String, Object>) map.get(locationKey));
			exit = LocationUtil.deserializeLocation((Map<String, Object>) map.get(exitKey));
			
			if (map.containsKey(exitPitchKey)) {
				exit.setPitch(((Number)map.get(exitPitchKey)).floatValue());
				exit.setYaw(((Number)map.get(exitYawKey)).floatValue());
			}
			
			if (map.containsKey(locationPitchKey)) {
				location.setPitch(((Number)map.get(locationPitchKey)).floatValue());
				location.setYaw(((Number)map.get(locationYawKey)).floatValue());
			}
			
			gateBlockLocations = new HashSet<Location>();
			List<Map<String, Object>> serializedGateBlocks = (List<Map<String, Object>>)map.get(gateBlocksKey);
			
			for (Map<String, Object> sgb : serializedGateBlocks) {
				gateBlockLocations.add(LocationUtil.deserializeLocation(sgb));
			}

            gateFrameBlocks = FloodUtil.getFrameWithLocations(gateBlockLocations);
		}
		catch (Exception e) {
			Plugin.log("ERROR: Failed to load gate '" + id + "'! (" + e.getMessage() + ")");
			Plugin.log("NOTE:  This gate will be removed from 'gates.yml' and added to 'invalid_gates.yml'!");
			
			Plugin.getPlugin().getGatesManager().storeInvalidGate(map);
		}
	}
	
	
	public Map<String, Object> serialize() 
	{
		try {
			validate(); // make sure to not write invalid stuff to disk
		} 
		catch (Exception e) {
			Plugin.log("Gate " + this.getId() + " seems to be not valid. It got closed before serializing! (Reason: " + e.getMessage() + ")");
		}
		
		Map<String, Object> retVal = new HashMap<String, Object>();
		
		retVal.put(idKey, id);
		retVal.put(locationKey, LocationUtil.serializeLocation(location));		
		retVal.put(exitKey, LocationUtil.serializeLocation(exit));
		retVal.put(isHiddenKey, isHidden);
		retVal.put(isOpenKey, isOpen);
		
		if (exit != null) {
			retVal.put(exitPitchKey, exit.getPitch());
			retVal.put(exitYawKey, exit.getYaw());
		}
		
		if (location != null) {
			retVal.put(locationPitchKey, location.getPitch());
			retVal.put(locationYawKey, location.getYaw());
		}
		
		List<Map<String, Object>> serializedGateBlocks = new ArrayList<Map<String, Object>>();
		
		for (Location l : gateBlockLocations) {
			serializedGateBlocks.add(LocationUtil.serializeLocation(l));
		}
		
		retVal.put(gateBlocksKey, serializedGateBlocks);
		
		return retVal;
	}
}
