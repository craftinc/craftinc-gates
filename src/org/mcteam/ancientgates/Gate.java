package org.mcteam.ancientgates;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Type;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import org.mcteam.ancientgates.gson.reflect.TypeToken;
import org.mcteam.ancientgates.util.DiscUtil;
import org.mcteam.ancientgates.util.FloodUtil;


public class Gate 
{
	private static transient TreeMap<String, Gate> instances = new TreeMap<String, Gate>(String.CASE_INSENSITIVE_ORDER);
	private static transient File file = new File(Plugin.instance.getDataFolder(), "gates.json");
	
	private transient String id;
	private Location from;
	private Location to;
	private boolean isHidden = false;
	private boolean isOpen = false; 
        
    private Integer[][] gateBlocks;

    
	public Gate() 
	{	
	}
	
	
	// -------------------------------------------- //
	// Getters And Setters
	// -------------------------------------------- //
	
	public void setId(String id) 
	{
		this.id = id;
	}

	
	public String getId() 
	{
		return id;
	}

	
	public void setFrom(Location from) 
	{
		this.from = from;
		setGateBlocks(FloodUtil.getGateFrameBlocks(from.getBlock()));
	}

	
	public Location getFrom() 
	{
		return from;
	}

	public void setTo(Location to) 
	{
		this.to = to;
	}

	
	public Location getTo() 
	{
		return to;
	}
     
	
    public Integer[][] getGateBlocks() 
    {
    	return gateBlocks;
    }

    
    private void setGateBlocks(Set<Block> gateBlocks) 
    {
        if (gateBlocks == null)
            return;

        this.gateBlocks = new Integer[gateBlocks.size()][3];

        int blockcount = 0;
        for (Block b : gateBlocks) 
        {
            if (b != null) 
            {
                this.gateBlocks[blockcount][0] = b.getX();
                this.gateBlocks[blockcount][1] = b.getY();
                this.gateBlocks[blockcount][2] = b.getZ();
            }
            blockcount++;
        }
    }
	
    
	//----------------------------------------------//
	// The Open And Close Methods
	//----------------------------------------------//
	
	public boolean open() 
	{
		Set<Block> blocks = FloodUtil.getGateFrameBlocks(from.getBlock());
		setGateBlocks(blocks);
		
		if (blocks == null)
		{
			return false;
		}
                
		if (isHidden() == false)
		{
			fillGate(blocks);
		}

		setOpen(true);
		
		return true;
	}
	
	
	private void fillGate(Set<Block> blocks)
	{
		// This is not to do an effect
		// It is to stop portal blocks from destroying themself as they cant rely on non created blocks :P
		for (Block block : blocks)
			block.setType(Material.GLOWSTONE);
		
		for (Block block : blocks)
			block.setType(Material.PORTAL);
	}
	
	
	public void close() 
	{
		removeGateBlocks();
		setOpen(false);
	}
	
	
	private void removeGateBlocks()
	{
		if (from != null)
		{
			Set<Block> blocks = FloodUtil.getGateFrameBlocks(from.getBlock());
	                
			if (blocks != null)
			{
				for (Block block : blocks)
					block.setType(Material.AIR);
			}
		}
	}
	
	
	//----------------------------------------------//
	// isHidden Setter and Getter
	//----------------------------------------------//
	
	public boolean setHidden(boolean isHidden)
	{
		this.isHidden = isHidden;
		
		if (isHidden == true)
		{
			removeGateBlocks();
		}
		else if (this.isOpen && !open()) 
		{
			// cannot open that gate (no frame!)
			this.isHidden = false;
			return false;
		}

		return true;
	}
	
	
	public boolean isHidden()
	{
		return this.isHidden;
	}
	
	
	//----------------------------------------------//
	// isOpen Setter and Getter
	//----------------------------------------------//
	
	private void setOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
	}
	
	
	public boolean isOpen()
	{
		// check if gate is really open
		if (getGateBlocks() == null)
		{
			isOpen = false;
		}
		else if (!isHidden())
		{
			Integer[] gateBlock = getGateBlocks()[0];
			Block b = new Location(from.getWorld(), gateBlock[0], gateBlock[1], gateBlock[2]).getBlock();
			
			if (b.getType() != Material.PORTAL)
			{
				isOpen = false;
			}
		}
		
		return this.isOpen;
	}
	
	
	//----------------------------------------------//
	// Persistance and entity management
	//----------------------------------------------//
	
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
		//faction.save();
		return gate;
	}
	
	
	public static void rename(Gate gate, String newId)
	{
		delete(gate.id);
		
		gate.setId(newId);
		instances.put(gate.id, gate);
	}
	
	
	public static void delete(String id) 
	{
		// Remove the faction
		instances.remove(id);
	}
	
	
	public static boolean save() 
	{
		try 
		{
			DiscUtil.write(file, Plugin.gson.toJson(instances));
		} 
		catch (IOException e) 
		{
			Plugin.log("Failed to save the gates to disk due to I/O exception.");
			e.printStackTrace();
			return false;
		} 
		catch (NullPointerException e) 
		{
			Plugin.log("Failed to save the gates to disk due to NPE.");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public static boolean load() 
	{
		Plugin.log("Loading gates from disk");
		if ( ! file.exists()) {
			Plugin.log("No gates to load from disk. Creating new file.");
			save();
			return true;
		}
		
		try 
		{
			Type type = new TypeToken<Map<String, Gate>>(){}.getType();
			Map<String, Gate> instancesFromFile = Plugin.gson.fromJson(DiscUtil.read(file), type);
			instances.clear();
			instances.putAll(instancesFromFile);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		fillIds();
			
		// old releases did not save gate blocks - this fixes the problem
		for (Gate g : getAll())
		{
	    	if (g.getGateBlocks() == null && g.getFrom() != null)
	    	{
	    		Plugin.log("Fixing problems with old gate: " + g.getId());
	    		
	    		Set<Block> gateBlocks = FloodUtil.getGateFrameBlocks(g.getFrom().getBlock());
	    		
	    		if (gateBlocks == null)
	    			continue;
	    		
	    		g.setGateBlocks(gateBlocks);
	    		
	    		if (((Block) gateBlocks.toArray()[0]).getType() == Material.PORTAL )
	    			g.setOpen(true);
	    	}
	    		
		}
		save();
		// end of fix
		
		return true;
	}
	
	
	public static Collection<Gate> getAll() 
	{
		return instances.values();
	}
	
	
	public static void fillIds() 
	{
		for(Entry<String, Gate> entry : instances.entrySet()) 
			entry.getValue().setId(entry.getKey());
	}
}
