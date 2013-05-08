package de.craftinc.gates;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftinc.gates.commands.*;
import de.craftinc.gates.listeners.PluginPlayerListener;


public class Plugin extends JavaPlugin 
{
	public static final String permissionInfo = "craftincgates.info";
	public static final String permissionManage = "craftincgates.manage";
	public static final String permissionUse = "craftincgates.use";
	
	private static Plugin instance;
	private static Permission permission;
	
	private String baseCommand;
	private PluginPlayerListener playerListener = new PluginPlayerListener();
	private List<BaseCommand> commands = new ArrayList<BaseCommand>();
	private GatesManager gatesManager = new GatesManager();
	
	
	public Plugin()
	{
		instance = this;
	}
	
	
	public static Plugin getPlugin()
	{
		return instance;
	}
	
	
	public GatesManager getGatesManager() 
	{
		return gatesManager;
	}


	@Override
	public void onLoad() 
	{
		ConfigurationSerialization.registerClass(Gate.class);
	}
	
	
	private void setupPermissions()
	{
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		
		if (rsp != null)
		{
			log("Using permission provider provided by Vault.");
			permission = rsp.getProvider();
		}
		else 
		{
			log("Not using setup permission provider provided by Vault.");
		}
	}
	

	@Override
	public void onDisable() 
	{
		// Save gates
		gatesManager.saveGatesToDisk();
		
		log("Disabled");
	}

	
	@Override
	public void onEnable() 
	{
		// Setup permissions
		setupPermissions();
		
		// Add the commands
		commands.add(new CommandHelp());
		commands.add(new CommandCreate());
		commands.add(new CommandDelete());
		commands.add(new CommandSetLocation());
		commands.add(new CommandSetExit());
		commands.add(new CommandOpen());
		commands.add(new CommandRename());
		commands.add(new CommandClose());
		commands.add(new CommandList());
		commands.add(new CommandInfo());
		commands.add(new CommandSetHidden());
		commands.add(new CommandSetVisible());

		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		
		// Load gates
		gatesManager.loadGatesFromDisk();
		
		log("Enabled");
	}

	
	// -------------------------------------------- //
	// Commands
	// -------------------------------------------- //
	
	public String getBaseCommand()
	{
		if (this.baseCommand != null)
			return this.baseCommand;
		
		Map<String, Map<String, Object>> Commands = this.getDescription().getCommands();
		
		this.baseCommand = Commands.keySet().iterator().next();
		
		return this.baseCommand;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
		List<String> parameters = new ArrayList<String>(Arrays.asList(args));
		this.handleCommand(sender, parameters);
		return true;
	}
	
	
	public void handleCommand(CommandSender sender, List<String> parameters) 
	{
		if (parameters.size() == 0) 
		{
			this.commands.get(0).execute(sender, parameters);
			return;
		}
		
		String commandName = parameters.get(0).toLowerCase();
		parameters.remove(0);
		
		for (BaseCommand fcommand : this.commands) 
		{
			if (fcommand.getAliases().contains(commandName)) 
			{
				fcommand.execute(sender, parameters);
				return;
			}
		}
		
		sender.sendMessage("Unknown gate-command \"" + commandName + "\". Try " + "/" + getBaseCommand() + " help");
	}
	
	
	/* 
	 * Logging
	 */
	public static void log(String msg) 
	{
		log(Level.INFO, msg);
	}
	
	
	public static void log(Level level, String msg) 
	{
		Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}


	public static Permission getPermission() {
		return permission;
	}
}
