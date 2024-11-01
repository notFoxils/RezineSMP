package me.foxils.rezineSMP.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class GlaxFireball extends LargeFireball {

    public GlaxFireball(@NotNull Location bukkitLocation, @NotNull Vector launchVelocity, @NotNull World world) {
        super(EntityType.FIREBALL, ((CraftWorld) world).getHandle());

        this.setPos(new Vec3(bukkitLocation.toVector().toVector3f()));
        this.setDeltaMovement(new Vec3(launchVelocity.toVector3f()));

        level().addFreshEntity(this);
    }

    public GlaxFireball(@NotNull Location bukkitLocation, @NotNull Vector launchVelocity, @NotNull World world, @NotNull org.bukkit.entity.Entity owner) {
        this(bukkitLocation, launchVelocity, world);

        final Entity ownerAsEntityNMS = ((CraftEntity) owner).getHandle();

        this.setOwner(ownerAsEntityNMS);
    }

    @Override
    protected void onHitEntity(EntityHitResult movingobjectpositionentity) {
        final Entity entityHit = movingobjectpositionentity.getEntity();

        final Entity fireballShooter = this.getOwner();

        if (fireballShooter != null && fireballShooter.getUUID() == entityHit.getUUID()) return;

        // Set hit entity on fire for 1 minute
        // Fire bypasses fire resistance
        // The entityHit cannot be extinguished by water

        super.onHitEntity(movingobjectpositionentity);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        // Make lava pool on ground

        final BlockPos hitBlockPosition = blockHitResult.getBlockPos();

        //for ()
    }
}
