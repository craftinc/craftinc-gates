package de.craftinc.gates;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class GatesManager 
{
	private File gatesConfigFile;
	private FileConfiguration gatesConfig;
	private String gatesPath = "gates"; // path to gates inside the yaml file
	
	private Map<String, Gate> gatesById;
	private Map<Chunk, Set<Gate>> gatesByChunk;
	private Map<Location, Gate> gatesByLocation;
	
	private List<Gate> gates;
	
	
	public Gate getGateWithId(String id)
	{
		return gatesById.get(id);
	}
	
	
	public Set<Gate> getGatesInsideChunk(Chunk c)
	{
		return gatesByChunk.get(c);
	}
	
	
	public Gate getGateAtLocation(Location l)
	{
		return gatesByLocation.get(l);
	}
	
	
	public void saveGatesToDisk()
	{
		gatesConfig.set(gatesPath, new ArrayList<Object>(gatesById.values()));
		
		try {
			gatesConfig.save(gatesConfigFile);
			Plugin.log("Saved gates to disk.");
		} 
		catch (IOException e) {
			Plugin.log("ERROR: Could not save gates to disk.");
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadGatesFromDisk()
	{
		this.gatesConfigFile = new File(Plugin.getPlugin().getDataFolder(), "gates.yml");
		
		if(!this.gatesConfigFile.exists()) {
			try {
				this.gatesConfigFile.createNewFile();
			} catch (IOException e) {
				Plugin.log(Level.SEVERE, "Cannot create gate config file! No gates will be persisted.");
			}
		}
		
		this.gatesConfig = YamlConfiguration.loadConfiguration(gatesConfigFile);
		this.gates = (List<Gate>)gatesConfig.getList(gatesPath);
		
		for (Object o : this.gates) {
			
			if (!(o instanceof Gate)) {
				Plugin.log(Level.SEVERE, "Gate file on disk is invalid. No gates loaded.");
				// TODO: gates.yml will be empty after save/reload/server stop! All gates will be lost! No user will expect this!
				this.gates = new ArrayList<Gate>();
				break;
			}
		}
		
		fillGatesById();
		fillGatesByChunk();
		fillGatesByLocation();		
	}
	
	
	private void fillGatesById()
	{
		gatesById = new HashMap<String, Gate>((int)(gates.size() * 1.25));
		
		for (Gate g : gates) {
			gatesById.put(g.getId(), g);
		}
	}
	
	
	private void fillGatesByChunk()
	{
		HashSet<Chunk> chunksUsedByGates = new HashSet<Chunk>(gates.size());
		
		for (Gate g : gates) {
			chunksUsedByGates.add(g.getLocation().getChunk());
		}
		
		gatesByChunk = new HashMap<Chunk, Set<Gate>>((int)(chunksUsedByGates.size() * 1.25));
		
		for (Gate g : gates) {
			Chunk c = g.getLocation().getChunk();
			Set<Gate> gatesForC = gatesByChunk.get(c);
			
			if (gatesForC == null) {
				gatesForC = new HashSet<Gate>(); // NOTE: not optimizing size here
			}
			
			gatesForC.add(g);
		}
	}
	
	
	private void fillGatesByLocation()
	{
		int numGateBlocks = 0;
		
		for (Gate g : gates) {
			numGateBlocks += g.gateBlockLocations.size();
		}
		
		gatesByLocation = new HashMap<Location, Gate>((int)(numGateBlocks*1.25));
		
		for (Gate g : gates) {
			gatesByLocation.put(g.getLocation(), g);
		}
	}
	
	
	public void storeInvalidGate(Map<String, Object> map)
	{
		File invalidGatesFile = new File(Plugin.getPlugin().getDataFolder(), "invalid_gates.yml");
		Boolean invalidGatesFileExists = invalidGatesFile.exists();
		
		try {
			FileWriter fileWriter = new FileWriter(invalidGatesFile, true);
			
			if (!invalidGatesFileExists) {
				fileWriter.write("gates:\n");
			}
			
			fileWriter.write("- ==: ");
			fileWriter.write(map.get("==").toString() + "\n");
			map.remove("==");
			
			fileWriter.write("\topen: false\n");
			map.remove("open");
			
			fileWriter.write("\tgateBlocks: []\n");
			map.remove("gateBlocks");
			
			
			for (String key : map.keySet()) {
				Object value = map.get(key);
				
				fileWriter.write("\t" + key + ": ");
				
				if (value instanceof Map) {
					fileWriter.write("\n");
					
					@SuppressWarnings("unchecked")
					Map<String, Object> valueMap = (Map<String, Object>)value;
					
					for (String k : valueMap.keySet()) {
						Object v = valueMap.get(k);
					
						fileWriter.write("\t\t" + k + ": " + v.toString() + "\n");
					}

				}
				else {
					fileWriter.write(value.toString() + "\n");
				}
				
			}
			
			fileWriter.close();
		}
		catch (IOException e) {
			Plugin.log("ERROR: Could not save invalid gates to disk. Reason: \n" + e.getStackTrace());
		}
	}
	
	
	public void handleGateIdChange(Gate g, String oldId)
	{
		gatesById.remove(oldId);
		gatesById.put(g.getId(), g);
	}
	
	
	public void handleGateLocationChange(Gate g, Location oldLocation)
	{
		gatesByLocation.remove(oldLocation);
		gatesByLocation.put(g.getLocation(), g);
		
		
		gatesByChunk.get(oldLocation.getChunk()).remove(g);
		
		Set<Gate> newChunkGates = gatesByChunk.get(g.getLocation().getChunk());
		
		if (newChunkGates == null) {
			newChunkGates = new HashSet<Gate>(); // NOTE: not optimizing size here
		}
		
		newChunkGates.add(g);
		gatesByChunk.put(g.getLocation().getChunk(), newChunkGates);		
	}
	
	
	public void handleNewGate(Gate g)
	{
		// TODO: implement!
	}
	
	
	public void handleDeletion(Gate g)
	{
		// TODO: implement!
	}
	
	
	public boolean gateExists(String id)
	{
		return gatesById.containsKey(id);
	}
	
	
	public List<Gate> allGates ()
	{
		return gates;
	}
}
