package de.craftinc.gates;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftinc.gates.commands.*;
import de.craftinc.gates.listeners.PluginBlockListener;
import de.craftinc.gates.listeners.PluginPlayerListener;
import de.craftinc.gates.listeners.PluginPortalListener;


public class Plugin extends JavaPlugin 
{
	public static Plugin instance;
	
	public static final String permissionInfo = "craftincgates.info";
	public static final String permissionManage = "craftincgates.manage";
//	public static final String permissionAll = "craftincgates.*";
	public static final String permissionUse = "craftincgates.use";
	
	public static Permission permission = null;
	
	public PluginPlayerListener playerListener = new PluginPlayerListener();
	public PluginBlockListener blockListener = new PluginBlockListener();
	public PluginPortalListener portalListener = new PluginPortalListener();
	
	private File gatesConfigFile;
	private FileConfiguration gatesConfig;
	
	private String baseCommand;
	
	private String gatesPath = "gates";
	
	
	// Commands
	public List<BaseCommand> commands = new ArrayList<BaseCommand>();
	
	
	public Plugin()
	{
		instance = this;
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
		saveGates();
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
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.portalListener, this);
		
		// Load gates
		this.gatesConfigFile  = new File(getDataFolder(), "gates.yml");
		
		if(!this.gatesConfigFile.exists()) 
		{
			try {
				this.gatesConfigFile.createNewFile();
			} catch (IOException e) {
				log(Level.SEVERE, "Cannot create gate config file! No gates will be persisted.");
			}
		}
		
		this.gatesConfig = YamlConfiguration.loadConfiguration(gatesConfigFile);
		
		loadGates();
		
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
	
	
	public static void log(Level level, String msg) {
		Logger.getLogger("Minecraft").log(level, "["+instance.getDescription().getFullName()+"] "+msg);
	}
	
	/*
	 * Saving and Loading Gates
	 */
	public void loadGates() 
	{
		File gatesFile = new File(getDataFolder(), "gates.yml");
		FileConfiguration gatesConfig = YamlConfiguration.loadConfiguration(gatesFile);
		
		gatesConfig.getList(gatesPath); // this will create all the gates
	}
	
	
	public void saveGates()
	{
		gatesConfig.set(gatesPath, new ArrayList<Object>(Gate.getAll()));
		
		try {
			gatesConfig.save(gatesConfigFile);
			log("Saved gates to disk.");
		} 
		catch (IOException e) {
			log("ERROR: Could not save gates to disk.");
			e.printStackTrace();
		}
	}
	
	
	public void storeInvalidGate(Map<String, Object> map)
	{
		File invalidGatesFile = new File(getDataFolder(), "invalid_gates.yml");
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
			log("ERROR: Could not save invalid gates to disk.");
			e.printStackTrace();
		}
	}
}
