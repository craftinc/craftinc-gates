package de.craftinc.gates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import de.craftinc.gates.util.LocationSerializer;



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
	
	public Gate(String id) throws Exception
	{
		setId(id);
	}
	
	
	/*
	 * SETTER & GETTER
	 */
	
	public String getId() 
	{
		return id;
	}



	public void setId(String id) throws Exception
	{
		if (exists(id)) {
			throw new Exception("A gate with '" + id + "' already exists");
		}

		this.id = id;
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
		isHidden = (Boolean)map.get(isHiddenKey);
		isOpen = (Boolean)map.get(isOpenKey);
		
		try {
			location = LocationSerializer.deserializeLocation((Map<String, Object>) map.get(locationKey));
			exit = LocationSerializer.deserializeLocation((Map<String, Object>) map.get(exitKey));
			
			gateBlockLocations = new HashSet<Location>();
			List<Map<String, Object>> serializedGateBlocks = (List<Map<String, Object>>)map.get(gateBlocksKey);
			
			for (Map<String, Object> sgb : serializedGateBlocks) {
				gateBlockLocations.add(LocationSerializer.deserializeLocation(sgb));
			}
		}
		catch (Exception e) {
			Plugin.log("ERROR: Failed to load gate '" + id + "'! (" + e.getMessage() + ")");
			Plugin.log("NOTE:  This gate will be removed from 'gates.yml' and added to 'invalid_gates.yml'!");
			
			Plugin.instance.storeInvalidGate(map);

			return;
		}
		
		instances.put(id, this);
	}
	
	
	public Map<String, Object> serialize() 
	{
		try {
			validate(); // make sure to not write invalid stuff to disk
		} 
		catch (Exception e) {
		}
		
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
	
	
	public static Gate create(String id) throws Exception
	{
		Gate gate = new Gate(id);
		
		instances.put(gate.id, gate);
		return gate;
	}
	
	
	public static void rename(String oldId, String newId) throws Exception
	{
		Gate gate = get(oldId);
		
		gate.setId(newId);
		
		delete(oldId);
		instances.put(gate.id, gate);
	}
	
	
	public static void delete(String id) 
	{
		Gate g = get(id);
		
		if (g != null) {
			g.emptyGate();
		}
		
		instances.remove(id);
	}
	
	
	public static Collection<Gate> getAll() 
	{
		return instances.values();
	}
}
