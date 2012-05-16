package org.mcteam.ancientgates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.mcteam.ancientgates.util.LocationSerializer;



/**
 * Adds persistence and serialization to the base gate class.
 */
public class Gate extends BaseGate implements ConfigurationSerializable
{
	/*
	 * ATTRIBUTES
	 */
	
	protected String id;
	
	protected static Map<String, Gate> instances = new HashMap<String, Gate>();
	
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Gate()
	{
	}
	
	
	/*
	 * SETTER & GETTER
	 */
	
	public String getId() 
	{
		return id;
	}


	/**
	 * @return true if the id has been changed; false otherwise
	 */
	public boolean setId(String id)
	{
		if (!exists(id)) {
			this.id = id;
			return true;
		}
		
		return false;
	}


	/*
	 * INTERFACE: ConfigurationSerializable
	 */
	static String idKey = "id";
	static String locationKey = "location";
	static String gateBlocksKey = "gateBlocks";
	static String exitKey = "exit";
	static String isHiddenKey = "hidden";
	static String isOpenKey = "open";
	
	
	@SuppressWarnings("unchecked")
	public Gate(Map<String, Object> map) 
	{
		id = (String)map.get(idKey);
		location = LocationSerializer.deserializeLocation((Map<String, Object>) map.get(locationKey));
		exit = LocationSerializer.deserializeLocation((Map<String, Object>) map.get(exitKey));
		isHidden = (Boolean)map.get(isHiddenKey);
		isOpen = (Boolean)map.get(isOpenKey);
		
		gateBlockLocations = new HashSet<Location>();
		List<Map<String, Object>> serializedGateBlocks = (List<Map<String, Object>>)map.get(gateBlocksKey);
		
		for (Map<String, Object> sgb : serializedGateBlocks) {
			gateBlockLocations.add(LocationSerializer.deserializeLocation(sgb));
		}
		
		instances.put(id, this);
	}
	
	
	public Map<String, Object> serialize() 
	{
		validate(); // make sure to not write invalid stuff to disk
		
		Map<String, Object> retVal = new HashMap<String, Object>();
		
		retVal.put(idKey, id);
		retVal.put(locationKey, LocationSerializer.serializeLocation(location));
		retVal.put(exitKey, LocationSerializer.serializeLocation(exit));
		retVal.put(isHiddenKey, isHidden);
		retVal.put(isOpenKey, isOpen);
		
		List<Map<String, Object>> serializedGateBlocks = new ArrayList<Map<String, Object>>();
		
		for (Location l : gateBlockLocations) {
			serializedGateBlocks.add(LocationSerializer.serializeLocation(l));
		}
		
		retVal.put(gateBlocksKey, serializedGateBlocks);
		
		return retVal;
	}


	/*
	 * ENTITY MANAGEMENT
	 */
	
	public static Gate get(String id) 
	{
		return instances.get(id);
	}
	
	
	public static boolean exists(String id) 
	{
		return instances.containsKey(id);
	}
	
	
	public static Gate create(String id) 
	{
		Gate gate = new Gate();
		gate.id = id;
		instances.put(gate.id, gate);
		Plugin.log("created new gate " + gate.id);
		
		return gate;
	}
	
	
	public static void rename(Gate gate, String newId) throws Exception
	{
		String oldId = gate.id;
		
		gate.setId(newId);
		
		delete(oldId);
		instances.put(gate.id, gate);
	}
	
	
	public static void delete(String id) 
	{
		instances.remove(id);
	}
	
	
	public static Collection<Gate> getAll() 
	{
		return instances.values();
	}
}
