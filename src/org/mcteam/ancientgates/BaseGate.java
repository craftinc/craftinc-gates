package org.mcteam.ancientgates;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.mcteam.ancientgates.util.FloodUtil;



public abstract class BaseGate
{
	/*
	 * ATTRIBUTES
	 */
	protected Location location; /* saving both location and gateBlockLocations is redundant but makes it easy to allow players to reshape gates */
	protected Set<Location> gateBlockLocations; /* Locations of the blocks inside the gate */
	
	protected Location exit;
	
	protected boolean isHidden;
	protected boolean isOpen;
	
	
	/*
	 * SETTER & GETTER
	 */	
	
	public Location getLocation() 
	{
		return location;
	}
	
	
	public void setLocation(Location location) 
	{
		this.location = location;
		
		if (isOpen) {
			findPortalBlocks();
			validate();
		}
	}
	
	
	public Location getExit() 
	{
		return exit;
	}
	
	
	public void setExit(Location exit) 
	{
		this.exit = exit;
		validate();
	}
	
	
	public boolean isHidden() 
	{
		return isHidden;
	}
	
	
	public void setHidden(boolean isHidden)
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
	
	
	public void setOpen(boolean isOpen) 
	{
		if (isOpen == true && this.isOpen == false) {
			findPortalBlocks();
			
			if (!isHidden) {
				fillGate();
			}
			
			validate();
		}
		else if (isOpen == false && this.isOpen == true) {
			emptyGate();
		}
		
		this.isOpen = isOpen;
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
		Set<Block> gateBlocks = FloodUtil.getGateFrameBlocks(location.getBlock());
		
		if (gateBlocks == null) {
			gateBlockLocations = null;
		}
		else {
			gateBlockLocations = new HashSet<Location>();
			
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
	protected void validate() 
	{
		if (!isOpen) {
			return;
		}
		
		if (location == null || exit == null) {
			isOpen = false;
			emptyGate();
			return;
		}
		
		if (isHidden == false) {
			for (Location l : gateBlockLocations) {
				if (l.getBlock().getType() != Material.PORTAL) {
					isOpen = false;
					emptyGate();
					return;
				}
			}
		}	
	}
}
