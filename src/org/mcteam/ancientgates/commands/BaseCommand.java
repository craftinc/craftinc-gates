package org.mcteam.ancientgates.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;
import org.mcteam.ancientgates.Conf;
import org.mcteam.ancientgates.util.TextUtil;

public class BaseCommand 
{
	public static final String permissionInfo = "ancientgates.info";
	public static final String permissionManage = "ancientgates.manage";
	
	public List<String> aliases;
	public List<String> requiredParameters;
	public List<String> optionalParameters;
	
	public String helpDescription;
	
	public CommandSender sender;
	public boolean senderMustBePlayer;
	public boolean hasGateParam;
	public Player player;
	public Gate gate;
	
	public List<String> parameters;
	
	
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
		
		if ( ! validateCall()) {
			return;
		}
		
		if (this.senderMustBePlayer) {
			this.player = (Player)sender;
		}
		
		perform();
	}
	
	public void perform() {
		
	}
	
	public void sendMessage(String message) {
		sender.sendMessage(Conf.colorSystem+message);
	}
	
	public void sendMessage(List<String> messages) {
		for(String message : messages) {
			this.sendMessage(message);
		}
	}
	
	public boolean validateCall() 
	{
		// validate player		
		if ( this.senderMustBePlayer && ! (sender instanceof Player)) 
		{
			sendMessage("This command can only be used by ingame players.");
			return false;
		}
		
		// validate permission
		if( ! hasPermission(sender)) 
		{
			sendMessage("You lack the permissions to "+this.helpDescription.toLowerCase()+".");
			return false;
		}
		
		// valide parameter count
		if (parameters.size() < requiredParameters.size()) 
		{
			sendMessage("Usage: "+this.getUseageTemplate(true));
			return false;
		}
		
		// validate gate parameter
		if (this.hasGateParam) 
		{
			String id = parameters.get(0);
			
			if ( ! Gate.exists(id)) 
			{
				sendMessage("There exists no gate with id "+id);
				return false;
			}
			gate = Gate.get(id);
		}
		
		return true;
	}
	
	public boolean hasPermission(CommandSender sender) 
	{
		return false; // make sure to overwrite this in all subclasses!
	}
	
	// -------------------------------------------- //
	// Help and usage description
	// -------------------------------------------- //
	public String getUsageTemplate(boolean withColor, boolean withDescription) {
		String ret = "";
		
		if (withColor) {
			ret += Conf.colorCommand;
		}
		
		ret += "/" + Plugin.instance.getBaseCommand()+ " " +TextUtil.implode(this.getAliases(), ",")+" ";
		
		List<String> parts = new ArrayList<String>();
		
		for (String requiredParameter : this.requiredParameters) {
			parts.add("["+requiredParameter+"]");
		}
		
		for (String optionalParameter : this.optionalParameters) {
			parts.add("*["+optionalParameter+"]");
		}
		
		if (withColor) {
			ret += Conf.colorParameter;
		}
		
		ret += TextUtil.implode(parts, " ");
		
		if (withDescription) {
			ret += "  "+Conf.colorSystem + this.helpDescription;
		}
		return ret;
	}
	
	public String getUseageTemplate(boolean withColor) {
		return getUsageTemplate(withColor, false);
	}
	
	public String getUseageTemplate() {
		return getUseageTemplate(true);
	}
}
