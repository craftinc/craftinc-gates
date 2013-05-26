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

import de.craftinc.gates.util.SimpleChunk;
import de.craftinc.gates.util.SimpleLocation;


public class GatesManager 
{
	private File gatesConfigFile;
	private FileConfiguration gatesConfig;
	private static final String gatesPath = "gates"; // path to gates inside the yaml file
    private static final String storageVersionPath = "version";
    private static final int storageVersion = 1;
	
	private Map<String, Gate> gatesById;
	private Map<SimpleChunk, Set<Gate>> gatesByChunk;
	private Map<SimpleLocation, Gate> gatesByLocation;
	
	private List<Gate> gates;
	
	
	public Gate getGateWithId(String id)
	{
		return gatesById.get(id);
	}
	
	
	public Set<Gate> getNearbyGates(Chunk chunk)
	{
		SimpleChunk simpleChunk = new SimpleChunk(chunk);
		return gatesByChunk.get(simpleChunk);
	}
	
	
	public Gate getGateAtLocation(Location location)
	{
		SimpleLocation simpleLocation = new SimpleLocation(location);
		return gatesByLocation.get(simpleLocation);
	}
	
	
	public void saveGatesToDisk()
	{
		gatesConfig.set(gatesPath, new ArrayList<Object>(gatesById.values()));
        gatesConfig.set(storageVersionPath, storageVersion);
		
		try {
			gatesConfig.save(gatesConfigFile);
			Plugin.log("Saved gates to disk.");
		} 
		catch (IOException e) {
			Plugin.log(Level.SEVERE, "ERROR: Could not save gates to disk.");
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

        Plugin.log("Loaded " + this.gates.size() + " gates.");

        // migration
        int fileStorageVersion = gatesConfig.getInt(storageVersionPath);

        if (fileStorageVersion < storageVersion) {
            Plugin.log("Outdated storage version detected. Performing data migration...");
            MigrationUtil.performMigration(fileStorageVersion, storageVersion, this.gates);
        }
	}
	
	
	private void fillGatesById()
	{
		gatesById = new HashMap<String, Gate>((int)(gates.size() * 1.25));
		
		for (Gate g : gates) {
			this.addGateWithId(g);
		}
	}
	
	
	private void fillGatesByChunk()
	{
		HashSet<Chunk> chunksUsedByGates = new HashSet<Chunk>();
		
		for (Gate g : gates) {

            if (g.getLocation() != null) {
			    chunksUsedByGates.add(g.getLocation().getChunk());
            }
		}
		
		gatesByChunk = new HashMap<SimpleChunk, Set<Gate>>((int)(chunksUsedByGates.size() * 1.25));
		
		for (Gate g : gates) {
			this.addGateByChunk(g);
		}
	}
	
	
	private void fillGatesByLocation()
	{
		int numGateBlocks = 0;
		
		for (Gate g : gates) {
			numGateBlocks += g.gateBlockLocations.size();
		}
		
		gatesByLocation = new HashMap<SimpleLocation, Gate>((int)(numGateBlocks*1.25));
		
		for (Gate g : gates) {
			this.addGateByLocations(g);
		}
	}
	
	
	private void removeGateById(String id)
	{
		gatesById.remove(id);
	}
	
	
	private void addGateWithId(Gate g)
	{
		gatesById.put(g.getId(), g);
	}
	
	
	private void removeGateFromLocations(Set<Location> gateBlocks)
	{
		for (Location l : gateBlocks) {
			SimpleLocation sl = new SimpleLocation(l);
			gatesByLocation.remove(sl);
		}
	}
	
	
	private void addGateByLocations(Gate g)
	{
		for (Location l : g.getGateBlockLocations()) {
			SimpleLocation sl = new SimpleLocation(l);
			gatesByLocation.put(sl, g);
		}
	}
	
	
	private void removeGateFromChunk(Gate g, Location l)
	{
		if (l != null) {
            SimpleChunk sc = new SimpleChunk(l.getChunk());
		    Set<Gate> gatesInChunk = gatesByChunk.get(sc);
		    gatesInChunk.remove(g);
        }
	}
	
	
	private void addGateByChunk(Gate g)
	{
        Location gateLocation = g.getLocation();

		if (gateLocation != null) {
            SimpleChunk c = new SimpleChunk(gateLocation.getChunk());
            Set<Gate> gatesForC = gatesByChunk.get(c);

            if (gatesForC == null) {
                gatesForC = new HashSet<Gate>(); // NOTE: not optimizing size here
                gatesByChunk.put(c, gatesForC);
            }

            gatesForC.add(g);
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
			Plugin.log("ERROR: Could not save invalid gates to disk. Reason: \n" + Arrays.toString(e.getStackTrace()));
		}
	}
	
	
	public void handleGateIdChange(Gate g, String oldId)
	{
		this.removeGateById(oldId);
		this.addGateWithId(g);
	}
	
	
	public void handleGateLocationChange(Gate g, Location oldLocation, Set<Location> oldGateBlockLocations)
	{
		this.removeGateFromChunk(g, oldLocation);
		this.addGateByChunk(g);
		
		this.removeGateFromLocations(oldGateBlockLocations);
		this.addGateByLocations(g);
	}
	
	
	public void handleNewGate(Gate g)
	{
		this.addGateByChunk(g);
		this.addGateByLocations(g);
		this.addGateWithId(g);
	}
	
	
	public void handleDeletion(Gate g)
	{
		this.removeGateById(g.getId());
		this.removeGateFromChunk(g, g.getLocation());
		this.removeGateFromLocations(g.getGateBlockLocations());
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
