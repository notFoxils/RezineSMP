package me.foxils.rezineSMP.items;

import me.foxils.foxutils.Item;
import me.foxils.foxutils.itemactions.ClickActions;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.rezineSMP.utilities.EntityTracing;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;

public class DragonBlade extends Item implements ClickActions {

    @SuppressWarnings("all")
    private final NamespacedKey DRAGONS_CALL_COOLDOWN_KEY;

    public DragonBlade(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList) {
        super(material, customModelData, name, plugin, abilityList);

        this.DRAGONS_CALL_COOLDOWN_KEY = new NamespacedKey(plugin, "dragon_call_cooldown");
    }

    private void dragonsCallAbility(PlayerInteractEvent event, ItemStack itemStack) {
        final Player player = event.getPlayer();
        final LivingEntity selectedEntity = EntityTracing.getEntityLookingAt(player, 10);

        if (selectedEntity == null) return;

        final World world = player.getWorld();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            final Vector playerLocationVector = player.getEyeLocation().toVector();
            final Vector selectedEntityLocationVector = selectedEntity.getEyeLocation().toVector();

            final Vector playerToSelectedEntityDirectionVector = selectedEntityLocationVector.clone().subtract(playerLocationVector).normalize();

            for (float i = 0; i < playerLocationVector.distance(selectedEntityLocationVector); i += 0.25F) {
                final Vector particleLocationAsVector = playerLocationVector.clone().add(playerToSelectedEntityDirectionVector.clone().multiply(i));

                final Location particleLocation = new Location(world, particleLocationAsVector.getX(), particleLocationAsVector.getY(), particleLocationAsVector.getZ());

                world.spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(Color.BLUE, 0.5F));
            }
        }, 0L, 20L);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        dragonsCallAbility(event, itemInteracted);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, ItemStack itemInteracted) {
        dragonsCallAbility(event, itemInteracted);
    }
}
