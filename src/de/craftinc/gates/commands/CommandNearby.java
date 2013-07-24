package de.craftinc.gates.commands;


import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.GateBlockChangeSender;

public class CommandNearby extends BaseLocationCommand
{
    public CommandNearby()
    {
        aliases.add("nearby");
        aliases.add("nb");

        helpDescription = "Highlight nearby gates";

        requiredPermission = Plugin.permissionInfo;

        needsPermissionAtCurrentLocation = true;
        shouldPersistToDisk = false;
        senderMustBePlayer = true;
        hasGateParam = false;
    }


    public void perform()
    {
        GateBlockChangeSender.temporaryHighlightGatesFrames(player);
    }
}
