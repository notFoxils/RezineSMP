package me.foxils.rezineSMP.items;

import me.foxils.foxutils.itemactions.ClickActions;
import me.foxils.foxutils.itemactions.TakeDamageAction;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.foxutils.utilities.ItemUtils;
import me.foxils.rezineSMP.entities.GlaxFireball;
import me.foxils.rezineSMP.utilities.EntityAttributes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class FireGlaxRelement extends RelementItem implements TakeDamageAction, ClickActions {

    private final NamespacedKey GLAX_FIRE_BALL_ABILITY_COOLDOWN_KEY;
    private final NamespacedKey IN_FIRE_RESISTANCE_EFFECT_COOLDOWN_KEY;

    private static final BaseComponent DRAGON_FIREBALL_SHOOT_ABILITY_USAGE_MESSAGE =
            new ComponentBuilder()
                    .append("Shot a Glax Fireball").color(ChatColor.of("#c64600"))
                    .bold(true)
                    .build();

    private static final Set<EntityDamageEvent.DamageCause> FIRE_RELATED_DAMAGE_CAUSES = Set.of(
            EntityDamageEvent.DamageCause.HOT_FLOOR,
            EntityDamageEvent.DamageCause.CAMPFIRE,
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.FIRE_TICK
    );

    private static final PotionEffect IN_FIRE_BONUS_STRENGTH_EFFECT = new PotionEffect(PotionEffectType.STRENGTH, 200, 1, true, true);

    private static final PotionEffect IN_FIRE_BONUS_RESISTANCE_EFFECT = new PotionEffect(PotionEffectType.RESISTANCE, 600, 1, true, true);

    public FireGlaxRelement(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList) {
        super(material, customModelData, name, plugin, abilityList, 1, 4);

        GLAX_FIRE_BALL_ABILITY_COOLDOWN_KEY = new NamespacedKey(plugin, "glax_fireball_ability_cooldown");
        IN_FIRE_RESISTANCE_EFFECT_COOLDOWN_KEY = new NamespacedKey(plugin, "fire_resistance_effect_cooldown");
    }

    private void shootGlaxFireball(PlayerInteractEvent event, ItemStack itemStack) {
        final Player player = event.getPlayer();

        if (ItemUtils.getCooldown(GLAX_FIRE_BALL_ABILITY_COOLDOWN_KEY, itemStack, 1, player, DRAGON_FIREBALL_SHOOT_ABILITY_USAGE_MESSAGE)) return;

        final GlaxFireball glaxFireball = new GlaxFireball(player);

        final Fireball fireball = (Fireball) glaxFireball.getBukkitEntity();

        fireball.setVelocity(player.getEyeLocation().getDirection());

        player.playSound(player, Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5F, 0.5F), 6L);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        shootGlaxFireball(event, itemInteracted);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, ItemStack itemInteracted) {
        rightClickAir(event, itemInteracted);
    }

    private void addStrengthEffectWhenOnFire(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FIRE) return;

        final Player player = (Player) entityDamageEvent.getEntity();

        player.addPotionEffect(IN_FIRE_BONUS_STRENGTH_EFFECT);
    }

    private void preventFireDamage(EntityDamageEvent entityDamageEvent) {
        final EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();

        if (!FIRE_RELATED_DAMAGE_CAUSES.contains(damageCause) || damageCause != EntityDamageEvent.DamageCause.FIRE) return;

        entityDamageEvent.setCancelled(true);
    }

    private void speedBonusInLava(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.LAVA) return;

        final Player player = (Player) entityDamageEvent.getEntity();

        EntityAttributes.setEntityAttributeBaseValueForDuration(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.5D, 100L);
    }

    private void addResistanceEffectWhenOnFire(EntityDamageEvent entityDamageEvent, ItemStack itemStack) {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FIRE) return;

        if (ItemUtils.getCooldown(IN_FIRE_RESISTANCE_EFFECT_COOLDOWN_KEY, itemStack, 60)) return;

        final Player player = (Player) entityDamageEvent.getEntity();

        player.addPotionEffect(IN_FIRE_BONUS_RESISTANCE_EFFECT);
    }

    @Override
    public void onTakeDamage(EntityDamageEvent entityDamageEvent, ItemStack itemStack) {
        if (getItemStackLevel(itemStack) < 1) return;

        preventFireDamage(entityDamageEvent);
        addStrengthEffectWhenOnFire(entityDamageEvent);

        if (getItemStackLevel(itemStack) < 3) return;

        speedBonusInLava(entityDamageEvent);
        addResistanceEffectWhenOnFire(entityDamageEvent, itemStack);
    }

}
