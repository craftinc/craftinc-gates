package de.craftinc.gates.commands;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.GateBlockChangeSender;
import de.craftinc.gates.util.TextUtil;

import java.util.ArrayList;
import java.util.Set;

public class CommandNearby extends BaseLocationCommand {

    public CommandNearby() {
        aliases.add("nearby");
        aliases.add("nb");

        helpDescription = "Highlight nearby gates";
        requiredPermission = Plugin.permissionInfo;
        needsPermissionAtCurrentLocation = true;
        shouldPersistToDisk = false;
        senderMustBePlayer = true;
        hasGateParam = false;
    }

    public void perform() {
        GatesManager manager = Plugin.getPlugin().getGatesManager();
        Set<Gate> nearbyGates = manager.getNearbyGates(player.getLocation().getChunk());

        if (nearbyGates == null) {
            player.sendMessage("There are no gates near you!");
        } else {
            GateBlockChangeSender.temporaryHighlightGatesFrames(player, nearbyGates);

            ArrayList<String> gateNames = new ArrayList<>();

            for (Gate g : nearbyGates) {
                gateNames.add(g.getId());
            }
            player.sendMessage("Nearby gates: " + TextUtil.implode(gateNames, ", "));
        }
    }
}
