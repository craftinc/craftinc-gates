package de.craftinc.gates.commands;

import de.craftinc.gates.controllers.PermissionController;

public class CommandTeleport extends BaseCommand {

    public CommandTeleport() {
        aliases.add("teleport");
        aliases.add("t");

        requiredParameters.add("id");

        senderMustBePlayer = true;
        hasGateParam = true;
        helpDescription = "Teleport to the location of a gate";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = false;
    }

    public void perform() {
    }
}
