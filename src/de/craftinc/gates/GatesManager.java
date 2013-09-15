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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import java.util.logging.Level;

import de.craftinc.gates.persistence.MigrationUtil;
import de.craftinc.gates.util.ConfigurationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.craftinc.gates.util.SimpleChunk;
import de.craftinc.gates.util.SimpleLocation;


public class GatesManager 
{
    protected File gatesConfigFile;
    protected FileConfiguration gatesConfig;
    protected static final String gatesPath = "gates"; // path to gates inside the yaml file
    protected static final String storageVersionPath = "version";
    protected static final int storageVersion = 1;

    protected int chunkRadius;

    protected Map<String, Gate> gatesById;
    protected Map<SimpleChunk, Set<Gate>> gatesByChunk;
    protected Map<SimpleLocation, Gate> gatesByLocation;
    protected Map<SimpleLocation, Gate> gatesByFrameLocation;

    protected List<Gate> gates;

    protected boolean storageFileIsInvalid = false;


	public Gate getGateWithId(final String id)
	{
		return gatesById.get(id.toLowerCase());
	}
	
	
	public Set<Gate> getNearbyGates(final Chunk chunk)
	{
		SimpleChunk simpleChunk = new SimpleChunk(chunk);
		return gatesByChunk.get(simpleChunk);
	}
	
	
	public Gate getGateAtLocation(final Location location)
	{
		SimpleLocation simpleLocation = new SimpleLocation(location);
		return gatesByLocation.get(simpleLocation);
	}


    public Gate getGateAtFrameLocation(final Location location)
    {
        SimpleLocation simpleLocation = new SimpleLocation(location);
        return gatesByFrameLocation.get(simpleLocation);
    }

	
	public void saveGatesToDisk()
	{
		if (storageFileIsInvalid) {
            Plugin.log(Level.SEVERE, "ERROR: Not saving gates to disk. Storage file is invalid or corrupted!");
            return;
        }

        gatesConfig.set(gatesPath, gates);
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
	public boolean loadGatesFromDisk()
	{
		this.gatesConfigFile = new File(Plugin.getPlugin().getDataFolder(), "gates.yml");
		
		if(!this.gatesConfigFile.exists()) {
			try {
                boolean isNew = this.gatesConfigFile.createNewFile();

                if (isNew) {
                    Plugin.log(Level.FINEST, "Created gate storage file.");
                }
            }
            catch (IOException e) {
                this.storageFileIsInvalid = true;
				Plugin.log(Level.SEVERE, "Cannot create gate storage file! No gates will be persisted.");
                return false;
			}
		}
		
		this.gatesConfig = new YamlConfiguration();

        try {
            this.gatesConfig.load(this.gatesConfigFile);
        }
        catch (Exception e) {
            this.storageFileIsInvalid = true;
            Plugin.log(Level.SEVERE, "Gate file on disk is invalid. No gates loaded. Plugin will be disabled! (" + Arrays.toString(e.getStackTrace()) + ")");
            return false;
        }

		this.gates = (List<Gate>)gatesConfig.getList(gatesPath);

        if (this.gates == null) {
            this.gates = new ArrayList<Gate>();
        }

        for (Object o : this.gates) {
			
			if (!(o instanceof Gate)) {
                this.storageFileIsInvalid = true;
				Plugin.log(Level.SEVERE, "Gate file on disk is invalid. No gates loaded. Plugin will be disabled! (Invalid gate class detected)");
                return false;
        	}
		}

        for (Gate g : this.gates) {
            try {
                g.validate();
            }
            catch (Exception e) {
                try {
                    g.setOpen(false);
                }
                catch (Exception ignored) { }

                Plugin.log(Level.FINER, "closed gate '" + g.getId() + "' reason: " + e.getMessage());
            }
        }

		fillGatesById();
		fillGatesByChunk();
		fillGatesByLocation();
        fillGatesByFrameLocation();

        Plugin.log("Loaded " + this.gates.size() + " gates.");

        // migration
        int fileStorageVersion = gatesConfig.getInt(storageVersionPath);

        if (fileStorageVersion > storageVersion) {
            this.storageFileIsInvalid = true;
            Plugin.log(Level.SEVERE, "Unsupported storage version detected! Make sure you have the latest version of Craft Inc. Gates installed. Plugin will be disabled!");
            return false;
        }

        if (fileStorageVersion < storageVersion && !this.gates.isEmpty()) {
            Plugin.log("Outdated storage version detected. Performing data migration...");
            boolean success = MigrationUtil.performMigration(fileStorageVersion, storageVersion, this.gates);

            this.storageFileIsInvalid = !success;
            return success;
        }

        return true;
	}


    protected int getChunkRadius()
    {
        if (this.chunkRadius == 0) {
            this.chunkRadius = Plugin.getPlugin().getConfig().getInt(ConfigurationUtil.confPlayerGateBlockUpdateRadiusKey);
            this.chunkRadius = this.chunkRadius >> 4;
        }

        return this.chunkRadius;
    }


    protected void fillGatesById()
	{
		gatesById = new HashMap<String, Gate>((int)(gates.size() * 1.25));
		
		for (Gate g : gates) {
			this.addGateWithId(g);
		}
	}


    protected void fillGatesByChunk()
	{
		HashSet<SimpleChunk> chunksUsedByGates = new HashSet<SimpleChunk>();
		
		for (Gate g : gates) {

            if (g.getLocation() != null) {

                Chunk c = g.getLocation().getChunk();

                int x = c.getX();
                int z = c.getZ();

                for (int i = x-getChunkRadius(); i < x+getChunkRadius(); i++) {

                    for (int j = z-getChunkRadius(); j < z+getChunkRadius(); j++) {

                        chunksUsedByGates.add(new SimpleChunk(i, j, c.getWorld()));
                    }
                }
            }
		}
		
		gatesByChunk = new HashMap<SimpleChunk, Set<Gate>>((int)(chunksUsedByGates.size() * 1.25));
		
		for (Gate g : gates) {
			this.addGateByChunk(g);
		}
	}


    protected void fillGatesByLocation()
	{
		Set<Location> gateBlocks = new HashSet<Location>();
		
		for (Gate g : gates) {

            for (Location l : g.getGateBlockLocations()) {
                gateBlocks.add(l);

                Location headLocation = new Location(l.getWorld(),
                                                     l.getX(),
                                                     l.getY()+1,
                                                     l.getZ());

                gateBlocks.add(headLocation);
            }
		}
		
		gatesByLocation = new HashMap<SimpleLocation, Gate>((int)(gateBlocks.size()*1.25));
		
		for (Gate g : gates) {
			this.addGateByLocations(g);
		}
	}


    protected void fillGatesByFrameLocation()
    {
        int numFrameBlocks = 0;

        for (Gate g : gates) {
            numFrameBlocks += g.gateFrameBlocks.size();
        }

        gatesByFrameLocation = new HashMap<SimpleLocation, Gate>((int)(numFrameBlocks*1.25));

        for (Gate g : gates) {
            this.addGateByFrameLocations(g);
        }
    }


    protected void removeGateById(final String id)
	{
		gatesById.remove(id);
	}


    protected void addGateWithId(final Gate g)
	{
		gatesById.put(g.getId(), g);
	}


    protected void removeGateByLocation(final Set<Location> gateBlocks)
	{
		if (gateBlocks != null) {

            for (Location l : gateBlocks) {

                SimpleLocation sl = new SimpleLocation(l);
                gatesByLocation.remove(sl);

                SimpleLocation headLocation = new SimpleLocation(l, true);
                gatesByLocation.remove(headLocation);
            }
        }
	}


    protected void removeGateByFrameLocation(final Set<Block> gateFrameBlocks)
    {
        if (gateFrameBlocks != null) {

            for (Block block : gateFrameBlocks) {
                SimpleLocation sl = new SimpleLocation(block.getLocation());
                gatesByFrameLocation.remove(sl);
            }
        }
    }


    protected void addGateByLocations(final Gate g)
	{
        for (Location l : g.getGateBlockLocations()) {

			SimpleLocation sl = new SimpleLocation(l);
			gatesByLocation.put(sl, g);

            SimpleLocation headLocation = new SimpleLocation(l, true);
            gatesByLocation.put(headLocation, g);
		}
	}


    protected void addGateByFrameLocations(final Gate g)
    {
        for (Block block : g.getGateFrameBlocks()) {
            SimpleLocation sl = new SimpleLocation(block.getLocation());
            gatesByFrameLocation.put(sl, g);
        }
    }


    protected void removeGateFromChunk(final Gate g, final Location l)
	{
		if (l != null) {

            Chunk c = l.getChunk();
            int x = c.getX();
            int z = c.getZ();

            for (int i = x-getChunkRadius(); i < x+getChunkRadius(); i++) {

                for (int j = z-getChunkRadius(); j < z+getChunkRadius(); j++) {

                    SimpleChunk sc = new SimpleChunk(i, j, c.getWorld());
		            Set<Gate> gatesInChunk = gatesByChunk.get(sc);

                    if (gatesInChunk != null) {
                        gatesInChunk.remove(g);
                    }

                }
            }
        }
	}


    protected void addGateByChunk(final Gate g)
	{
        Location gateLocation = g.getLocation();

		if (gateLocation != null) {

            Chunk c = g.getLocation().getChunk();
            int x = c.getX();
            int z = c.getZ();

            for (int i = x-getChunkRadius(); i < x+getChunkRadius(); i++) {

                for (int j = z-getChunkRadius(); j < z+getChunkRadius(); j++) {

                    SimpleChunk sc = new SimpleChunk(i, j, c.getWorld());

                    Set<Gate> gatesForC = gatesByChunk.get(sc);

                    if (gatesForC == null) {
                        gatesForC = new HashSet<Gate>(); // NOTE: not optimizing size here
                        gatesByChunk.put(sc, gatesForC);
                    }

                    gatesForC.add(g);
                }
            }
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
	
	
	public void handleGateIdChange(final Gate g, final String oldId)
	{
		this.removeGateById(oldId);
		this.addGateWithId(g);
	}
	
	
	public void handleGateLocationChange(final Gate g,
                                         final Location oldLocation,
                                         final Set<Location> oldGateBlockLocations,
                                         final Set<Block> oldGateFrameBlocks)
	{
		this.removeGateFromChunk(g, oldLocation);
		this.addGateByChunk(g);
		
		this.removeGateByLocation(oldGateBlockLocations);
		this.addGateByLocations(g);

        this.removeGateByFrameLocation(oldGateFrameBlocks);
        this.addGateByFrameLocations(g);
	}
	
	
	public void handleNewGate(final Gate g)
	{
		this.gates.add(g);

        this.addGateByChunk(g);
		this.addGateByLocations(g);
		this.addGateWithId(g);
        this.addGateByFrameLocations(g);
	}
	
	
	public void handleDeletion(final Gate g)
	{
        this.gates.remove(g);

        this.removeGateById(g.getId());
		this.removeGateFromChunk(g, g.getLocation());
		this.removeGateByLocation(g.getGateBlockLocations());
        this.removeGateByFrameLocation(g.getGateFrameBlocks());
	}
	
	
	public boolean gateExists(final String id)
	{
		return gatesById.containsKey(id.toLowerCase());
	}
	
	
	public List<Gate> allGates ()
	{
		return gates;
	}
}
