package de.craftinc.gates.commands;


import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class CommandExitOpen extends BaseCommand
{
    public CommandExitOpen()
    {
        aliases.add("exitopen");
        aliases.add("eo");

        requiredParameters.add("id");

        helpDescription = "Change exit of location and open that gate afterwards.";

        requiredPermission = Plugin.permissionManage;

        needsPermissionAtCurrentLocation = true;
        shouldPersistToDisk = true;
        senderMustBePlayer = true;
    }


    public void perform()
    {
        try
        {
            gate.setExit(player.getLocation());
            sendMessage(ChatColor.GREEN + "The exit of gate '" + gate.getId() + "' is now where you stand.");

            try {
                boolean needsGateManagerUpdate = false;

                if (gate.getGateBlockLocations().isEmpty()) {
                    needsGateManagerUpdate = true;
                }

                gate.setOpen(true);

                GateBlockChangeSender.updateGateBlocks(gate);

                if (needsGateManagerUpdate) {
                    Plugin.getPlugin().getGatesManager().handleGateLocationChange(gate, null, null, null);
                }

                sendMessage(ChatColor.GREEN + "The gate was opened.");
            }
            catch (Exception e) {
                sendMessage(ChatColor.RED + e.getMessage());
            }
        }
        catch (Exception e) {
            GateBlockChangeSender.updateGateBlocks(gate);
            sendMessage(ChatColor.RED + "Setting the exit for the gate failed! This gate is now closed! (See server log for more information.)");
            Plugin.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
    }
}
