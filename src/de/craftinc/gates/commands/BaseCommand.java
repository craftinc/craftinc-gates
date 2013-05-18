package de.craftinc.gates.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

public abstract class BaseCommand 
{
	protected List<String> aliases = new ArrayList<String>();
	protected List<String> requiredParameters = new ArrayList<String>();
	protected List<String> optionalParameters = new ArrayList<String>();
	
	protected String helpDescription = "no description";
	
	protected List<String> parameters;
	protected CommandSender sender;
	protected Player player;
	protected Gate gate;
	
	protected boolean senderMustBePlayer = true;
	protected boolean hasGateParam = true; 
	
	protected String requiredPermission;
	protected boolean needsPermissionAtCurrentLocation;
	
	protected boolean shouldPersistToDisk;

	
	public List<String> getAliases() {
		return aliases;
	}
	
	
	public void execute(CommandSender sender, List<String> parameters) {
		this.sender = sender;
		this.parameters = parameters;
		
		if (!this.validateCall()) {
			return;
		}
		
		if (this.senderMustBePlayer) {
			this.player = (Player)sender;
		}
		
		this.perform();
		
		if (this.shouldPersistToDisk) {
			Plugin.getPlugin().getGatesManager().saveGatesToDisk();
		}
	}
	
	
	abstract protected void perform();
	
	
	protected void sendMessage(String message) {
		sender.sendMessage(message);
	}
	
	
	protected void sendMessage(List<String> messages) {
		for(String message : messages) {
			this.sendMessage(message);
		}
	}
	
	
	protected boolean validateCall() 
	{
		boolean allParamtertersThere = parameters.size() >= requiredParameters.size();
		boolean senderIsPlayer = this.sender instanceof Player;
		boolean hasGateParameter = false;
		
		if (this.hasGateParam && this.parameters.size() > 0 && this.setGateUsingParameter(this.parameters.get(0))) {
			hasGateParameter = true;
		}
		
		boolean senderHasPermission = this.hasPermission();
		boolean valid = false;
		
		if (this.senderMustBePlayer && !senderIsPlayer) {
			sendMessage(ChatColor.RED + "This command can only be used by ingame players.");
			valid = false;
		}
		else {
            if (!allParamtertersThere) {
                sendMessage(ChatColor.RED + "Some parameters are missing! " + ChatColor.AQUA + "Usage: " + this.getUsageTemplate(true));
                valid = false;
            } else if (!senderHasPermission && this.hasGateParam) {
                sendMessage(ChatColor.RED + "You either provided a invalid gate or do not have permission to " + this.helpDescription.toLowerCase());
                valid = false;
            } else if (!senderHasPermission) {
                sendMessage(ChatColor.RED + "You lack the permissions to " + this.helpDescription.toLowerCase());
                valid = false;
            } else if (this.hasGateParam && !hasGateParameter) {
                sendMessage(ChatColor.RED + "There exists no gate with id " + this.parameters.get(0));
                valid = false;
            } else {
                valid = true;
            }
        }
		
		return valid;
	}
	
	
	protected boolean setGateUsingParameter(String param)
	{
		GatesManager gateManager = Plugin.getPlugin().getGatesManager();
		
		if (!gateManager.gateExists(param)) {
			return false;
		}
		else {
			gate = gateManager.getGateWithId(param);
			return true;
		}
	}
	
	
	/**
	 * This will return false if a gate is required for this command but this.gate == null.
	 */
	protected boolean hasPermission()
	{		
		if (Plugin.getPermission() == null) { // fallback - use the standard bukkit permission system
			return this.sender.hasPermission(this.requiredPermission);
		}

		if (!(this.sender instanceof Player)) {
            // sender is no player - there is no information about the senders locations
            return Plugin.getPermission().has(this.sender, this.requiredPermission);
		}

		
		Player p = (Player) this.sender;
		boolean hasPermission = false;
		
		if (this.requiredPermission.equals(Plugin.permissionInfo)) {

			if (this.hasGateParam) {
				hasPermission = this.hasPermissionAtGateLocationAndExit(p);
			}
			else {
				hasPermission = Plugin.getPermission().has(p.getWorld(), p.getName(), this.requiredPermission);
			}
		}
		else if (this.requiredPermission.equals(Plugin.permissionUse) ) {
			hasPermission = this.hasPermissionAtGateLocationAndExit(p);
		}
		else if (this.requiredPermission.equals(Plugin.permissionManage)) {

			if (this.needsPermissionAtCurrentLocation && this.hasGateParam) {
				boolean hasPersmissionAtCurrentLocation = Plugin.getPermission().has(p.getWorld(), p.getName(), this.requiredPermission);
				hasPermission = hasPersmissionAtCurrentLocation && this.hasPermissionAtGateLocationAndExit(p);
			}
			else if (this.needsPermissionAtCurrentLocation) {
				hasPermission = Plugin.getPermission().has(p.getWorld(), p.getName(), this.requiredPermission);
			}
			else {
				hasPermission = this.hasPermissionAtGateLocationAndExit(p);
			}
		}
		
		return hasPermission;
	}
	
	
	protected boolean hasPermissionAtGateLocationAndExit(Player p)
	{
		if (this.gate == null || p == null) { // make sure we don't run into a nullpointer exception
			return false;
		}
		
		boolean permAtLocation = this.gate.getLocation() == null || Plugin.getPermission().has(this.gate.getLocation().getWorld(), p.getName(), this.requiredPermission);
		boolean permAtExit = this.gate.getExit() == null || Plugin.getPermission().has(this.gate.getExit().getWorld(), p.getName(), this.requiredPermission);

		return permAtLocation & permAtExit;
	}
	
	
	// -------------------------------------------- //
	// Help and usage description
	// -------------------------------------------- //
	protected String getUsageTemplate(boolean withColor, boolean withDescription)
    {
		String ret = "";
		
		if (withColor) {
			ret += ChatColor.AQUA;
		}
		
		ret += "/" + Plugin.getPlugin().getBaseCommand() + " " + TextUtil.implode(this.getAliases(), ",")+" ";
		
		List<String> parts = new ArrayList<String>();
		
		for (String requiredParameter : this.requiredParameters) {
			parts.add("["+requiredParameter+"]");
		}
		
		for (String optionalParameter : this.optionalParameters) {
			parts.add("*["+optionalParameter+"]");
		}
		
		if (withColor) {
			ret += ChatColor.DARK_AQUA;
		}
		
		ret += TextUtil.implode(parts, " ");
		
		if (withDescription) {
			ret += " ";
			
			if (withColor) {
				ret += ChatColor.YELLOW;
			}
			
			ret += this.helpDescription;
		}
		return ret;
	}
	
	protected String getUsageTemplate(boolean withColor)
    {
		return getUsageTemplate(withColor, false);
	}
	
	protected String getUsageTemplate()
    {
		return getUsageTemplate(true);
	}
}
