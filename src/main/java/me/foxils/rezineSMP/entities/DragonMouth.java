package me.foxils.rezineSMP.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DragonMouth extends LargeFireball {

    // Scrapped

    public DragonMouth(@NotNull Location bukkitLocation, @NotNull Vector launchVelocity, @NotNull World world) {
        super(EntityType.FIREBALL, ((CraftWorld) world).getHandle());

        super.isIncendiary = false;
        super.explosionPower = 0;

        setPos(new Vec3(bukkitLocation.toVector().toVector3f()));
        setDeltaMovement(new Vec3(launchVelocity.toVector3f()));
        setItem(new ItemStack(Items.DRAGON_HEAD));

        final Level currentLevel = level();

        if (currentLevel == null) return;

        currentLevel.addFreshEntity(this);
    }

    public DragonMouth(Player player) {
        this(player.getEyeLocation(), player.getEyeLocation().getDirection(), player.getWorld());

        setOwner(((CraftPlayer) player).getHandle());
    }

    @Override
    protected void onDeflection(@Nullable Entity entity, boolean flag) {}

    @Override
    protected void onHit(HitResult movingobjectposition) {
        HitResult.Type movingobjectposition_enummovingobjecttype = movingobjectposition.getType();

        if (movingobjectposition_enummovingobjecttype == HitResult.Type.ENTITY) {
            EntityHitResult movingobjectpositionentity = (EntityHitResult)movingobjectposition;

            this.onHitEntity(movingobjectpositionentity);

            this.level().gameEvent(GameEvent.PROJECTILE_LAND, movingobjectposition.getLocation(), GameEvent.Context.of(this, null));
        } else if (movingobjectposition_enummovingobjecttype == HitResult.Type.BLOCK) {
            BlockHitResult movingobjectpositionblock = (BlockHitResult)movingobjectposition;

            this.onHitBlock(movingobjectpositionblock);

            BlockPos blockposition = movingobjectpositionblock.getBlockPos();

            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockposition, GameEvent.Context.of(this, this.level().getBlockState(blockposition)));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult movingobjectpositionblock) {
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitEntity(EntityHitResult movingobjectpositionentity) {
        final Entity entity = movingobjectpositionentity.getEntity();

        if (!(entity instanceof LivingEntity)) return;

        if (entity == this.getOwner()) {
            this.remove(RemovalReason.DISCARDED);

            return;
        }

        setDeltaMovement(getDeltaMovement().reverse());
        entity.startRiding(this);
    }

}
