package de.craftinc.gates.commands;

import de.craftinc.gates.controllers.PermissionController;

public class CommandMaterial extends BaseCommand {

    public CommandMaterial() {
        aliases.add("material");
        aliases.add("m");

        requiredParameters.add("id");

        senderMustBePlayer = false;
        hasGateParam = true;
        helpDescription = "Change the material of a gate";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
    }

    public void perform() {
    }
}
