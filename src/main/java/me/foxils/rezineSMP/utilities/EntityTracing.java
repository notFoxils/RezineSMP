package me.foxils.rezineSMP.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityTracing {

    @Nullable
    public static LivingEntity getEntityLookingAt(@NotNull Player player, int distanceAhead) {
        final Location eyeLocation = player.getEyeLocation();

        final Vector playerLookDirection = eyeLocation.getDirection();

        final Location traceStartLocation = eyeLocation.add(playerLookDirection.clone().normalize().multiply(0.5));

        final RayTraceResult traceResult = player.getWorld().rayTraceEntities(traceStartLocation, playerLookDirection, distanceAhead);

        if (traceResult == null) return null;

        final Entity tracedEntity = traceResult.getHitEntity();

        if (tracedEntity == player) return null;

        if (!(tracedEntity instanceof LivingEntity livingEntity)) return null;

        return livingEntity;
    }
}
