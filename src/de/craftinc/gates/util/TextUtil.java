package de.craftinc.gates.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class TextUtil 
{
	public static String titleize(String str) 
	{
		String line = ChatColor.GOLD + repeat("_", 60);
		String center = ".[ " + ChatColor.YELLOW + str + ChatColor.GOLD + " ].";
		int pivot = line.length() / 2;
		int eatLeft = center.length() / 2;
		int eatRight = center.length() - eatLeft;
		
		return line.substring(0, pivot - eatLeft) + center + line.substring(pivot + eatRight);
	}
	
	
	public static String repeat(String s, int times) 
	{
	    if (times <= 0) 
	    	return "";
	    
	    return s + repeat(s, times-1);
	}
	
	
	/**
	 * Joins all emements of list into a single string, sperating the original strings with glue.
	 */
	public static String implode(List<String> list, String glue)
	{
	    if (list.size() == 0) {
	    	return "";
	    }
		
		String ret = list.get(0);
	    
	    for (int i=1; i<list.size(); i++) {
	        ret += glue + list.get(i);
	    }
	    
	    return ret;
	}
	
	/**
	 * Joins all emements of list into a single string.
	 */
	public static String implode(List<String> list) {
		return implode(list, "");
	}
	
	
	
	public static String getMaterialName(Material material) 
	{
		String ret = material.toString();
		ret = ret.replace('_', ' ');
		ret = ret.toLowerCase();
		
		return ret.substring(0, 1).toUpperCase() + ret.substring(1);
	}
}


