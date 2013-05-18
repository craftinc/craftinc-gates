package de.craftinc.gates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import de.craftinc.gates.util.FloodUtil;
import de.craftinc.gates.util.LocationUtil;


public class Gate implements ConfigurationSerializable
{
	/*
	 * ATTRIBUTES
	 */
	protected Location location; /* saving both location and gateBlockLocations is redundant but makes it easy to allow players to reshape gates */
	protected Set<Location> gateBlockLocations = new HashSet<Location>(); /* Locations of the blocks inside the gate */
	
	protected Location exit;
	
	protected boolean isHidden = false;
	protected boolean isOpen = false;
	
	protected String id;
	
	
	public Gate(String id)
	{
		setId(id);
	}
	
	
	public String toString()
	{
		return super.toString() + " " + this.getId();
	}
	
	
	/*
	 * SETTER & GETTER
	 */	
	
	public Location getLocation() 
	{
		return location;
	}
	
	
	public void setLocation(Location location)  throws Exception
	{
		this.location = location;
		
		if (isOpen) {
			fillGate();
			validate();
		}
	}
	
	
	public Location getExit() 
	{
		return exit;
	}
	
	
	public void setExit(Location exit)  throws Exception
	{
		this.exit = exit;
		validate();
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public boolean isHidden() 
	{
		return isHidden;
	}
	
	
	public void setHidden(boolean isHidden) throws Exception
	{
		this.isHidden = isHidden;
		
		if (isHidden) {
			emptyGate();
		}
		else if (isOpen()) {
			fillGate();
		}
		
		validate();
	}
	
	
	public boolean isOpen() 
	{
		return isOpen;
	}
	
	
	public void setOpen(boolean isOpen) throws Exception
	{
		if (isOpen && !this.isOpen) {
			findPortalBlocks();
			
			if (!isHidden) {
				fillGate();
			}
		}
		else if (!isOpen && this.isOpen) {
			emptyGate();
		}
		
		this.isOpen = isOpen;
		
		validate();
	}
	
	
	public Set<Location> getGateBlockLocations() 
	{
		return gateBlockLocations;
	}
	
	
	/*
	 * GATE BLOCK HANDLING
	 */
	
	protected void fillGate()
	{	
		emptyGate();
		findPortalBlocks();
		
		// This is not to do an effect
		// It is to stop portal blocks from destroying themself as they cant rely on non created blocks :P
		for (Location l : gateBlockLocations) {
			l.getBlock().setType(Material.GLOWSTONE);
		}
		
		for (Location l : gateBlockLocations) {
			l.getBlock().setType(Material.PORTAL);
		}
	}
	
	
	protected void emptyGate()
	{
		for (Location l : gateBlockLocations) {
			if (l.getBlock().getType() == Material.PORTAL) {
				l.getBlock().setType(Material.AIR);
			}
		}
	}
	
	
	protected void findPortalBlocks() 
	{
		gateBlockLocations = new HashSet<Location>();
		Set<Block> gateBlocks = FloodUtil.getGateFrameBlocks(location.getBlock());
		
		if (gateBlocks != null) {
			for (Block b : gateBlocks) {
				gateBlockLocations.add(b.getLocation());
			}
		}
	}
	
	
	/*
	 * VALIDATION
	 */
	
	/**
	 * Checks if valus attributes do add up; will close gate on wrong values.
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
			throw new Exception("Gate got closed. The frame is missing or broken.");
		}
		
		
		if (!isHidden) {
			for (Location l : gateBlockLocations) {
				if (l.getBlock().getType() == Material.AIR) {
					setOpen(false);
					throw new Exception("Gate got closed. The frame is missing or broken.");
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
    private Gate(Map<String, Object> map)
	{
		try {
			id = map.get(idKey).toString();
			isHidden = (Boolean)map.get(isHiddenKey);
			isOpen = (Boolean)map.get(isOpenKey);
			
			location = LocationUtil.deserializeLocation((Map<String, Object>) map.get(locationKey));
			exit = LocationUtil.deserializeLocation((Map<String, Object>) map.get(exitKey));
			
			if (map.containsKey(exitPitchKey)) {
				exit.setPitch(((Double)map.get(exitPitchKey)).floatValue());
				exit.setYaw(((Double)map.get(exitYawKey)).floatValue());
			}
			
			if (map.containsKey(locationPitchKey)) {
				location.setPitch(((Double)map.get(locationPitchKey)).floatValue());
				location.setYaw(((Double)map.get(locationYawKey)).floatValue());
			}
			
			gateBlockLocations = new HashSet<Location>();
			List<Map<String, Object>> serializedGateBlocks = (List<Map<String, Object>>)map.get(gateBlocksKey);
			
			for (Map<String, Object> sgb : serializedGateBlocks) {
				gateBlockLocations.add(LocationUtil.deserializeLocation(sgb));
			}
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
			Plugin.log("Gate " + this.getId() + " seems to be not valid. It got closed before serializing!");
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
