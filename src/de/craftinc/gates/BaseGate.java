package de.craftinc.gates;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import de.craftinc.gates.util.FloodUtil;


public abstract class BaseGate
{
	/*
	 * ATTRIBUTES
	 */
	protected Location location; /* saving both location and gateBlockLocations is redundant but makes it easy to allow players to reshape gates */
	protected Set<Location> gateBlockLocations = new HashSet<Location>(); /* Locations of the blocks inside the gate */
	
	protected Location exit;
	
	protected boolean isHidden = false;
	protected boolean isOpen = false;
	
	
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
	
	
	public boolean isHidden() 
	{
		return isHidden;
	}
	
	
	public void setHidden(boolean isHidden) throws Exception
	{
		this.isHidden = isHidden;
		
		if (isHidden == true) {
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
		if (isOpen == true && this.isOpen == false) {
			findPortalBlocks();
			
			if (!isHidden) {
				fillGate();
			}
		}
		else if (isOpen == false && this.isOpen == true) {
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
		
		if (gateBlockLocations == null || gateBlockLocations.size() == 0) {
			setOpen(false);
			throw new Exception("Gate got closed. The frame is missing or broken.");
		}
		
		
		if (isHidden == false) {			
			for (Location l : gateBlockLocations) {
				if (l.getBlock().getType() == Material.AIR) {
					setOpen(false);
					throw new Exception("Gate got closed. The frame is missing or broken.");
				}
			}
		}
	}
}
