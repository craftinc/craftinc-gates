package de.craftinc.gates.commands;

import de.craftinc.gates.controllers.PermissionController;
import de.craftinc.gates.models.GateMaterial;
import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;

import java.security.InvalidParameterException;

public class CommandMaterial extends BaseCommand {

    public CommandMaterial() {
        aliases.add("material");
        aliases.add("m");

        requiredParameters.add("id");
        requiredParameters.add("material");

        senderMustBePlayer = false;
        hasGateParam = true;
        helpDescription = "Change the material of a gate";
        requiredPermission = PermissionController.permissionManage;
        needsPermissionAtCurrentLocation = false;
        shouldPersistToDisk = true;
    }

    public void perform() {
        GateMaterial material;
        try {
            material = new GateMaterial(parameters.get(1));
        } catch (InvalidParameterException e) {
            sendMessage(ChatColor.RED + "Invalid material!");
            return;
        }

        try {
            gate.setMaterial(material);
        } catch (Exception e) {
            sendMessage(ChatColor.RED + "Frame invalid. Gate is now closed!");
        }

        GateBlockChangeSender.updateGateBlocks(gate);
        sendMessage(ChatColor.GREEN + "Gate " + gate.getId() + " uses now " + material.toString() + " as material.");
    }
}
