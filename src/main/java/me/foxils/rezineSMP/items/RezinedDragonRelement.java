package me.foxils.rezineSMP.items;

import me.foxils.foxutils.itemactions.ClickActions;
import me.foxils.foxutils.itemactions.DropAction;
import me.foxils.foxutils.itemactions.PassiveAction;
import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.foxutils.utilities.ItemUtils;
import me.foxils.rezineSMP.utilities.EntityAttributes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class RezinedDragonRelement extends RelementItem implements ClickActions, PassiveAction, DropAction {

    private final NamespacedKey DRAGON_LIFT_COOLDOWN_KEY;
    private final NamespacedKey REZINED_ANTI_ABILITY_COOLDOWN_KEY;
    private final NamespacedKey DRAGON_FIREBALL_RESTOCK_COOLDOWN_KEY;
    private final NamespacedKey DRAGON_FIREBALLS_AMOUNT_KEY;
    private final NamespacedKey DRAGON_FIREBALL_SHOOT_ABILITY_COOLDOWN_KEY;

    private static final BaseComponent DRAGON_LIFT_USAGE_MESSAGE =
            new ComponentBuilder()
                    .append("Activated Dragon Lift").color(ChatColor.DARK_PURPLE)
                    .bold(true)
                    .build();

    private static final BaseComponent DRAGON_LIFT_NO_PLAYERS_IN_RANGE_MESSAGE =
            new ComponentBuilder()
                    .append("No Players Found In Range").color(ChatColor.LIGHT_PURPLE)
                    .bold(true)
                    .build();

    private static final BaseComponent REZINED_ANTI_ABILITY_USAGE_MESSAGE =
            new ComponentBuilder()
                    .append("Activated Rezined Anti-Ability").color(ChatColor.RED)
                    .bold(true)
                    .build();

    private static final BaseComponent REZINED_ANTI_ABILITY_ABILITIES_DISABLED_MESSAGE =
            new ComponentBuilder()
                    .append("Your Abilities Have Been Disabled For 30s").color(ChatColor.GOLD)
                    .bold(true)
                    .build();

    private static final BaseComponent REZINED_ANTI_ABILITY_ABILITIES_ENABLED_MESSAGE =
            new ComponentBuilder()
                    .append("Your Abilities Have Been Restored").color(ChatColor.BLUE)
                    .bold(true)
                    .build();

    private static final BaseComponent DRAGON_FIREBALL_SHOOT_ABILITY_UNAVAILABLE_MESSAGE =
            new ComponentBuilder()
                    .append("No Fireballs Left").color(ChatColor.DARK_PURPLE)
                    .bold(true)
                    .build();

    private static final BaseComponent DRAGON_FIREBALL_SHOOT_ABILITY_USAGE_MESSAGE =
            new ComponentBuilder()
                    .append("Shot a Dragon Fireball").color(ChatColor.of("#c64600"))
                    .bold(true)
                    .build();

    private static final List<PotionEffect> PASSIVE_POTION_EFFECTS = List.of(
            new PotionEffect(PotionEffectType.STRENGTH, 200, 1, true, true)
    );

    public RezinedDragonRelement(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, List<ItemStack> itemsForRecipe, boolean shapedRecipe) {
        super(material, customModelData, name, plugin, abilityList, itemsForRecipe, shapedRecipe, 1, 4);

        this.DRAGON_LIFT_COOLDOWN_KEY = new NamespacedKey(plugin, "dragon_lift_cooldown");
        this.REZINED_ANTI_ABILITY_COOLDOWN_KEY = new NamespacedKey(plugin, "rezined_anti_ability_cooldown");
        this.DRAGON_FIREBALL_RESTOCK_COOLDOWN_KEY = new NamespacedKey(plugin, "dragon_fireball_restock_cooldown");
        this.DRAGON_FIREBALLS_AMOUNT_KEY= new NamespacedKey(plugin, "dragon_fireballs_amount");
        this.DRAGON_FIREBALL_SHOOT_ABILITY_COOLDOWN_KEY = new NamespacedKey(plugin, "dragon_fireball_shoot_ability_cooldown");
    }

    private void liftPlayersWithinFiveBlockRadius(PlayerInteractEvent event, ItemStack itemInteracted) {
        final Player player = event.getPlayer();

        if (ItemUtils.getCooldown(DRAGON_LIFT_COOLDOWN_KEY, itemInteracted, 60, player, DRAGON_LIFT_USAGE_MESSAGE)) return;

        final List<Entity> nearbyEntities = player.getNearbyEntities(2.5, 2.5, 2.5);

        nearbyEntities.removeIf(entity -> !(entity instanceof Player));

        if (nearbyEntities.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, DRAGON_LIFT_NO_PLAYERS_IN_RANGE_MESSAGE);
            return;
        }

        for (Entity playerNearby : nearbyEntities) {
            EntityAttributes.setEntityAttributeBaseValueForDuration(playerNearby, Attribute.GENERIC_MOVEMENT_SPEED, 0D, 100L);
            EntityAttributes.setEntityAttributeBaseValueForDuration(playerNearby, Attribute.PLAYER_ENTITY_INTERACTION_RANGE, 0D, 100L);
            EntityAttributes.setEntityAttributeBaseValueForDuration(playerNearby, Attribute.GENERIC_JUMP_STRENGTH, 0D, 100L);
            playerNearby.setGravity(false);

            Location currentLevitationLocation = playerNearby.getLocation().clone().add(0, 1, 0);

            playerNearby.teleport(currentLevitationLocation);
            playerNearby.getWorld().spawnParticle(Particle.POOF, currentLevitationLocation, 5);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                currentLevitationLocation.add(0, 2, 0);

                playerNearby.teleport(currentLevitationLocation);
                playerNearby.getWorld().spawnParticle(Particle.POOF, currentLevitationLocation, 5);
            }, 10L);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                currentLevitationLocation.add(0, 2, 0);

                playerNearby.teleport(currentLevitationLocation);
                playerNearby.getWorld().spawnParticle(Particle.PORTAL, currentLevitationLocation, 20);
                playerNearby.getWorld().spawnParticle(Particle.POOF, currentLevitationLocation, 5);
            }, 20L);

            // TODO: Add warning for Operators to allow flying on the server with the server.properties file
            final BukkitTask playerAntiMovementTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                final Location locationMovedTo = playerNearby.getLocation();

                if (locationMovedTo.distance(currentLevitationLocation) < 0.1) return;

                playerNearby.teleport(currentLevitationLocation.setDirection(locationMovedTo.getDirection()));
            }, 20L, 2L);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                playerNearby.setGravity(true);

                playerAntiMovementTask.cancel();
            }, 100L);
        }

        final World userPlayerWorld = player.getWorld();
        final Location userPlayerLocation = player.getLocation();

        userPlayerWorld.playSound(userPlayerLocation, Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 0.8F);
        userPlayerWorld.spawnParticle(Particle.POOF, userPlayerLocation, 10);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            userPlayerWorld.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 0.8F);
            userPlayerWorld.spawnParticle(Particle.POOF, userPlayerLocation, 10);
        }, 10L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            userPlayerWorld.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 0.8F);
            userPlayerWorld.spawnParticle(Particle.POOF, userPlayerLocation, 10);
        }, 20L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            userPlayerWorld.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1F, 0.8F);

            for (int i = 0; i < 5; i++) {
                userPlayerWorld.spawnParticle(Particle.PORTAL, userPlayerLocation.clone().add(0, i, 0), 20, 0.75, 0, 0.75);
                userPlayerWorld.spawnParticle(Particle.DUST, userPlayerLocation.clone().add(0, i, 0), 20, 0.75, 0, 0.75, new Particle.DustOptions(Color.PURPLE, 2));
            }
        }, 25L);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        if (getItemStackLevel(itemInteracted) < 1) return;

        liftPlayersWithinFiveBlockRadius(event, itemInteracted);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, ItemStack itemInteracted) {
        rightClickAir(event, itemInteracted);
    }

    private void disableAbilitiesForNearbyPlayers(PlayerInteractEvent event, ItemStack itemInteracted) {
        final Player player = event.getPlayer();

        if (ItemUtils.getCooldown(REZINED_ANTI_ABILITY_COOLDOWN_KEY, itemInteracted, 300, player, REZINED_ANTI_ABILITY_USAGE_MESSAGE)) return;

        final List<Entity> nearbyEntities = player.getNearbyEntities(5, 5, 5);

        nearbyEntities.removeIf(entity -> !(entity instanceof Player));

        for (Entity entityPlayerNearby : nearbyEntities) {
            final Player playerNearby = (Player) entityPlayerNearby;

            final PlayerInventory playerInventory = playerNearby.getInventory();

            final BukkitTask showDisabledAbilitiesParticles = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                final Location playerNearbyLocation = playerNearby.getLocation();

                playerNearby.getWorld().spawnParticle(Particle.ASH, playerNearbyLocation.add(0, 3, 0), 20, 0.5, 0, 0.5);
                playerNearby.getWorld().playSound(playerNearbyLocation, Sound.BLOCK_LAVA_EXTINGUISH, 0.15F, 0.8F);
            }, 0, 20);

            for (ItemStack itemStack : playerInventory.getContents()) {
                if (!(ItemRegistry.getItemFromItemStack(itemStack) instanceof RelementItem)) continue;

                RelementItem.setAbilitiesEnabled(itemStack, false);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    RelementItem.setAbilitiesEnabled(itemStack, true);

                    playerNearby.spigot().sendMessage(ChatMessageType.ACTION_BAR, REZINED_ANTI_ABILITY_ABILITIES_ENABLED_MESSAGE);
                }, 600L);
            }

            playerNearby.spigot().sendMessage(ChatMessageType.ACTION_BAR, REZINED_ANTI_ABILITY_ABILITIES_DISABLED_MESSAGE);

            Bukkit.getScheduler().runTaskLater(plugin, showDisabledAbilitiesParticles::cancel, 600L);
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.75F, 1.15F);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.75F, 0.8F);
    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        if (getItemStackLevel(itemInteracted) < 2) return;

        disableAbilitiesForNearbyPlayers(event, itemInteracted);
    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, ItemStack itemInteracted) {
        shiftRightClickAir(event, itemInteracted);
    }

    private void shootDragonBall(PlayerDropItemEvent playerDropItemEvent, ItemStack itemStack) {
        final Player player = playerDropItemEvent.getPlayer();

        if (!ItemUtils.getCooldown(DRAGON_FIREBALL_RESTOCK_COOLDOWN_KEY, itemStack, 60)) {
            ItemUtils.storeIntegerData(DRAGON_FIREBALLS_AMOUNT_KEY, itemStack, 5);
        }

        if (ItemUtils.getCooldown(DRAGON_FIREBALL_SHOOT_ABILITY_COOLDOWN_KEY, itemStack, 1, player, DRAGON_FIREBALL_SHOOT_ABILITY_USAGE_MESSAGE)) return;

        Integer amountOfFireballs = ItemUtils.getIntegerDataFromWeaponKey(DRAGON_FIREBALLS_AMOUNT_KEY, itemStack);

        if (amountOfFireballs == null || amountOfFireballs <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, DRAGON_FIREBALL_SHOOT_ABILITY_UNAVAILABLE_MESSAGE);

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 0.75F), 5L);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 0.5F), 10L);
            return;
        }

        ItemUtils.storeIntegerData(DRAGON_FIREBALLS_AMOUNT_KEY, itemStack, --amountOfFireballs);

        player.launchProjectile(DragonFireball.class);

        player.playSound(player, Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5F, 0.5F), 6L);
    }

    @Override
    public void dropItemAction(PlayerDropItemEvent playerDropItemEvent, ItemStack itemStack) {
        if (getItemStackLevel(itemStack) < 3) return;

        shootDragonBall(playerDropItemEvent, itemStack);
    }

    private void grantPassivePotionEffects(Player player) {
        player.addPotionEffects(PASSIVE_POTION_EFFECTS);
    }

    @Override
    public void passiveAction(Player player, ItemStack itemStack) {
        if (getItemStackLevel(itemStack) < 3) return;

        grantPassivePotionEffects(player);
    }
}
