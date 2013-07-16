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

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import de.craftinc.gates.listeners.*;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftinc.gates.commands.*;
import org.mcstats.Metrics;


public class Plugin extends JavaPlugin 
{
	public static final String permissionInfo = "craftincgates.info";
	public static final String permissionManage = "craftincgates.manage";
	public static final String permissionUse = "craftincgates.use";

    public static final String confMaxGateBlocksKey = "maxGateBlocks";
    public static final String confPlayerGateBlockUpdateRadiusKey = "playerGateBlockUpdateRadius";
    public static final String confCheckForBrokenGateFramesKey = "checkForBrokenGateFrames";
    public static final String confGateTeleportMessageKey = "gateTeleportMessage";
    public static final String confShowTeleportMessageKey = "showTeleportMessage";
    public static final String confGateTeleportNoPermissionMessageKey = "gateTeleportNoPermissionMessage";
    public static final String confShowTeleportNoPermissionMessageKey = "showTeleportNoPermissionMessage";
    public static final String confSaveOnChangesKey = "saveOnChanges";
	
	private static Plugin instance;
	private static Permission permission;
	
	protected String baseCommand;
    protected List<BaseCommand> commands = new ArrayList<BaseCommand>();
    protected GatesManager gatesManager = new GatesManager();

    protected PlayerMoveListener moveListener = new PlayerMoveListener();
    protected PlayerTeleportListener teleportListener = new PlayerTeleportListener();
    protected PlayerRespawnListener respawnListener = new PlayerRespawnListener();
    protected PlayerChangedWorldListener worldChangeListener = new PlayerChangedWorldListener();
    protected PlayerJoinListener joinListener = new PlayerJoinListener();
    protected BlockBreakListener blockBreakListener = new BlockBreakListener();
	
	
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


    protected void setupPermissions()
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
		// Setup Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            log("Failed to start metrics!");
        }

		// Setup configuration
        this.saveDefaultConfig();

		// Setup permissions
		setupPermissions();
		
		// Add the commands
		commands.add(new CommandHelp());
		commands.add(new CommandNew());
		commands.add(new CommandRemove());
		commands.add(new CommandLocation());
		commands.add(new CommandExit());
		commands.add(new CommandOpen());
		commands.add(new CommandRename());
		commands.add(new CommandClose());
		commands.add(new CommandList());
		commands.add(new CommandInfo());
		commands.add(new CommandHide());
		commands.add(new CommandUnhide());
        commands.add(new CommandExitOpen());

		
		// Register events
		this.registerEventListeners();
		
		// Load gates
		boolean success = gatesManager.loadGatesFromDisk();

        if (success) {
            log("Enabled");
        }
        else {
            PluginManager pm = this.getServer().getPluginManager();
            pm.disablePlugin(this);
        }
	}


    protected void registerEventListeners()
    {
        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(this.moveListener, this);
        pm.registerEvents(this.teleportListener, this);
        pm.registerEvents(this.respawnListener, this);
        pm.registerEvents(this.worldChangeListener, this);
        pm.registerEvents(this.joinListener, this);

        if (getConfig().getBoolean(confCheckForBrokenGateFramesKey)) {
            pm.registerEvents(this.blockBreakListener, this);
        }
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
		
		sender.sendMessage(ChatColor.RED + "Unknown gate-command \"" + commandName + "\"." +
                           ChatColor.GREEN + " Try " + "/" + getBaseCommand() + " help");
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
