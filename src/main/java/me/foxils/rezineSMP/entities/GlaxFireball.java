package me.foxils.rezineSMP.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GlaxFireball extends LargeFireball {

    private UUID antiDamagePlayerUUID = null;

    public GlaxFireball(@NotNull Location bukkitLocation, @NotNull Vector launchVelocity, @NotNull World world) {
        super(EntityType.FIREBALL, ((CraftWorld) world).getHandle());

        setPos(new Vec3(bukkitLocation.toVector().toVector3f()));
        setDeltaMovement(new Vec3(launchVelocity.toVector3f()));

        level().addFreshEntity(this);
    }

    public GlaxFireball(Player player) {
        this(player.getEyeLocation(), player.getEyeLocation().getDirection(), player.getWorld());

        this.antiDamagePlayerUUID = player.getUniqueId();
    }

    @Override
    protected void onHitEntity(EntityHitResult movingobjectpositionentity) {
        final Entity entity = movingobjectpositionentity.getEntity();

        if (entity.getUUID() == antiDamagePlayerUUID) return;

        // Set hit entity on fire for 1 minute
        // Fire bypasses fire resistance
        // The entity cannot be extinguished by water

        super.onHitEntity(movingobjectpositionentity);
    }

    @Override
    protected void onHitBlock(BlockHitResult movingobjectpositionblock) {
        // Make lava pool on ground
    }
}
