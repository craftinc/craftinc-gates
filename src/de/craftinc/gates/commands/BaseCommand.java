package de.craftinc.gates.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;

public abstract class BaseCommand 
{
	protected List<String> aliases;
	protected List<String> requiredParameters;
	protected List<String> optionalParameters;
	
	protected String helpDescription;
	
	protected List<String> parameters;
	protected CommandSender sender;
	protected Player player;
	protected Gate gate;
	
	protected boolean senderMustBePlayer;
	protected boolean hasGateParam;
	
	protected String requiredPermission;
	protected boolean needsPermissionAtCurrentLocation;
	
	
	public BaseCommand() 
	{
		aliases = new ArrayList<String>();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		
		senderMustBePlayer = true;
		hasGateParam = true;
		
		helpDescription = "no description";
	}
	
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
		boolean allParamtertersThere = parameters.size() < requiredParameters.size();
		boolean senderIsPlayer = this.sender instanceof Player;
		boolean parameterIsGate = this.parameters.size() > 0 ? this.getGateForParamater(this.parameters.get(0)) : false;
		boolean senderHasPermission;
		
		try {
			senderHasPermission = this.hasPermission();
		} 
		catch (Exception e) { // the gate paramter is missing or incorrect!
			senderHasPermission = parameterIsGate ? false : true; // only display the lack of permission message if there is a gate
																  // this should prevent giving permission to the user if there is
																  // a bug inside the permission validation code.
		}
		

		if(!senderHasPermission) 
		{
			sendMessage("You lack the permissions to " + this.helpDescription.toLowerCase() + ".");
			return false;
		}
		
			
		if (this.senderMustBePlayer && !senderIsPlayer) 
		{
			sendMessage("This command can only be used by ingame players.");
			return false;
		}
		
		if (this.hasGateParam && !parameterIsGate) 
		{
			sendMessage("There exists no gate with id " + this.parameters.get(0));
			return false;
		}
	
		if (allParamtertersThere) 
		{
			sendMessage("Usage: " + this.getUseageTemplate(true));
			return false;
		}
		
		return true;
	}
	
	
	protected boolean getGateForParamater(String param)
	{
		if (!Gate.exists(param))
		{
			return false;
		}
		else
		{
			gate = Gate.get(param);
			return true;
		}
	}
	
	
	
	protected boolean hasPermission() throws Exception
	{		
		if (Plugin.permission == null) // fallback Ð use the standard bukkit permission system
		{
			return this.sender.hasPermission(this.requiredPermission);
		}
		
		
		if (this.requiredPermission.equals(Plugin.permissionInfo))
		{
			return Plugin.permission.has(this.player.getWorld(), this.player.getName(), this.requiredPermission);
		}
		
		
		if (this.requiredPermission.equals(Plugin.permissionUse) )
		{
			return this.hasPermissionAtGateLocationAndExit();
		}
			
		
		if (this.requiredPermission.equals(Plugin.permissionManage))
		{
			if (this.needsPermissionAtCurrentLocation && this.hasGateParam)
			{
				boolean hasPersmissionAtCurrentLocation = Plugin.permission.has(this.player.getWorld(), this.player.getName(), this.requiredPermission);
				return hasPersmissionAtCurrentLocation && this.hasPermissionAtGateLocationAndExit();
			}
			else if (this.needsPermissionAtCurrentLocation)
			{
				return Plugin.permission.has(this.player.getWorld(), this.player.getName(), this.requiredPermission);
			}
			else
			{
				return this.hasPermissionAtGateLocationAndExit();
			}
		}
			
		
		return false;
	}
	
	
	protected boolean hasPermissionAtGateLocationAndExit() throws Exception
	{
		if (this.gate == null) // make sure we don't run into a nullpointer exception
		{
			throw new Exception("Cannot check permissons with no gate provided!");
		}
		
		System.out.println("************");
		System.out.println(this.gate.getLocation().getWorld());
		System.out.println(this.gate.getExit().getWorld());
		System.out.println("************");
		
		boolean permAtLocation = Plugin.permission.has(this.gate.getLocation().getWorld(), player.getName(), this.requiredPermission);
		boolean permAtExit = Plugin.permission.has(this.gate.getExit().getWorld(), player.getName(), this.requiredPermission);
		
		return permAtLocation && permAtExit;
	}
	
	
	// -------------------------------------------- //
	// Help and usage description
	// -------------------------------------------- //
	protected String getUsageTemplate(boolean withColor, boolean withDescription) {
		String ret = "";
		
//		if (withColor) {
//			ret += Conf.colorCommand;
//		}
		
		ret += "/" + Plugin.instance.getBaseCommand() + " " + TextUtil.implode(this.getAliases(), ",")+" ";
		
		List<String> parts = new ArrayList<String>();
		
		for (String requiredParameter : this.requiredParameters) {
			parts.add("["+requiredParameter+"]");
		}
		
		for (String optionalParameter : this.optionalParameters) {
			parts.add("*["+optionalParameter+"]");
		}
		
//		if (withColor) {
//			ret += Conf.colorParameter;
//		}
		
		ret += TextUtil.implode(parts, " ");
		
//		if (withDescription) {
//			ret += "  "+Conf.colorSystem + this.helpDescription;
//		}
		return ret;
	}
	
	protected String getUseageTemplate(boolean withColor) {
		return getUsageTemplate(withColor, false);
	}
	
	protected String getUseageTemplate() {
		return getUseageTemplate(true);
	}
}
