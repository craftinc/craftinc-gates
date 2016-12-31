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
package de.craftinc.gates.util;

import org.bukkit.Location;
import org.bukkit.entity.*;


public class VehicleCloner {

    public static Vehicle clone(Vehicle parent, Location cloneLocation) {
        Vehicle clone = cloneLocation.getWorld().spawn(cloneLocation, parent.getClass());

        clone.setFallDistance(parent.getFallDistance());
        clone.setFireTicks(parent.getFireTicks());
        clone.setVelocity(parent.getVelocity());
        clone.setTicksLived(parent.getTicksLived());
        clone.setLastDamageCause(parent.getLastDamageCause());

        if (clone instanceof Boat) {
            Boat boat = (Boat) clone;
            Boat parentBoat = (Boat) parent;

            boat.setMaxSpeed(parentBoat.getMaxSpeed());
            boat.setOccupiedDeceleration(parentBoat.getOccupiedDeceleration());
            boat.setUnoccupiedDeceleration(parentBoat.getUnoccupiedDeceleration());
            boat.setWorkOnLand(parentBoat.getWorkOnLand());
            boat.setVelocity(parentBoat.getVelocity());
        } else if (clone instanceof Animals) {
            Animals animal = (Animals) clone;
            Animals parentAnimal = (Animals) parent;

            animal.setMaxHealth(parentAnimal.getMaxHealth());
            animal.setHealth(parentAnimal.getMaxHealth());
            animal.setRemainingAir(parentAnimal.getRemainingAir());
            animal.setMaximumAir(parentAnimal.getMaximumAir());
            animal.setMaximumNoDamageTicks(parentAnimal.getMaximumNoDamageTicks());
            animal.setLastDamage(parentAnimal.getLastDamage());
            animal.setNoDamageTicks(parentAnimal.getNoDamageTicks());
            animal.addPotionEffects(parentAnimal.getActivePotionEffects());
            animal.setRemoveWhenFarAway(parentAnimal.getRemoveWhenFarAway());
            animal.setCanPickupItems(parentAnimal.getCanPickupItems());
            animal.setCustomName(parentAnimal.getCustomName());
            animal.setCustomNameVisible(parentAnimal.isCustomNameVisible());
            animal.setTarget(parentAnimal.getTarget());
            animal.setAge(parentAnimal.getAge());
            animal.setAgeLock(parentAnimal.getAgeLock());

            if (clone instanceof Horse) {
                Horse horse = (Horse) clone;
                Horse parentHorse = (Horse) parent;

                horse.getInventory().setArmor(parentHorse.getInventory().getArmor());
                horse.getInventory().setSaddle(parentHorse.getInventory().getSaddle());
                horse.setCarryingChest(parentHorse.isCarryingChest());
                horse.getInventory().setContents(parentHorse.getInventory().getContents());
                horse.setTamed(parentHorse.isTamed());
                horse.setOwner(parentHorse.getOwner());
                horse.setJumpStrength(parentHorse.getJumpStrength());
                horse.setMaxDomestication(parentHorse.getMaxDomestication());
                horse.setDomestication(parentHorse.getDomestication());
                horse.setStyle(parentHorse.getStyle());
                horse.setColor(parentHorse.getColor());
                horse.setVariant(parentHorse.getVariant());

                if (parentHorse.isAdult()) {
                    horse.setAdult();
                } else {
                    horse.setBaby();
                }

                horse.setBreed(parentHorse.canBreed());
            } else if (clone instanceof Pig) {
                Pig pig = (Pig) clone;
                Pig parentPig = (Pig) parent;

                pig.setSaddle(parentPig.hasSaddle());
            }
        } else if (clone instanceof Minecart) {
            Minecart minecart = (Minecart) clone;
            Minecart parentMinecart = (Minecart) parent;

            minecart.setDerailedVelocityMod(parentMinecart.getDerailedVelocityMod());
            minecart.setFlyingVelocityMod(parentMinecart.getFlyingVelocityMod());
            minecart.setSlowWhenEmpty(parentMinecart.isSlowWhenEmpty());
            minecart.setMaxSpeed(parentMinecart.getMaxSpeed());
            minecart.setDamage(parentMinecart.getDamage());
            minecart.setVelocity(parentMinecart.getVelocity());
        }

        return clone;
    }

}
