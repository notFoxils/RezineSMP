package me.foxils.rezineSMP;

import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.foxutils.utilities.ActionType;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.rezineSMP.commands.GetItemLevel;
import me.foxils.rezineSMP.commands.SetItemLevel;
import me.foxils.rezineSMP.items.DragonBlade;
import me.foxils.rezineSMP.items.RezinedDragonRelement;
import me.foxils.rezineSMP.items.FireGlaxRelement;
import me.foxils.rezineSMP.items.TestLevelableItem;
import me.foxils.rezineSMP.utilities.EntityAttributes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class RezineSMP extends JavaPlugin {

    private static final MiniMessage minimessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        EntityAttributes.initEntityAttributes(this);

        registerEvents();
        registerCustomItems();
        registerCommands();
    }

    private void registerEvents() {
    }

    private void registerCustomItems() {
        final var dragonRezinedRelementName = minimessage.deserialize("<b><gradient:#00fbff:#372869:#ae00ff:#690069>Rezined Dragon Relement</gradient></b>");
        final var windRelementName = minimessage.deserialize("<b><gradient:#55ffff:#969696:#c9c9c9:#ffffff>Wind Relement</gradient></b>");
        final var fireGlaxRelementName = minimessage.deserialize("<b><gradient:#c64600:#e01b24:#e66100:#a51d2d:#a51d2d:#ff7800>Fire Glax Relement</gradient></b>");
        final var retrixsRelementName = minimessage.deserialize("<b><gradient:#3d3846:#9a9996:#3d3846>Retrixs Relement</gradient></b>");

        final LegacyComponentSerializer legacySection = LegacyComponentSerializer.legacySection();

        ItemRegistry.registerItem(new RezinedDragonRelement(Material.PAPER, 1, legacySection.serialize(dragonRezinedRelementName), this,
                List.of(
                        new ItemAbility("test", List.of("nothing to see here"), ActionType.NONE)
                ),
                Arrays.asList(
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.TOTEM_OF_UNDYING),
                        new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.NETHERITE_INGOT),
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.BREEZE_ROD), new ItemStack(Material.TOTEM_OF_UNDYING)
                ), true));

        ItemRegistry.registerItem(new TestLevelableItem(Material.PAPER, 1, legacySection.serialize(windRelementName), this,
                List.of(
                        new ItemAbility("e", List.of("#e"), ActionType.RIGHT_CLICK),
                        new ItemAbility("e", List.of("#e"), ActionType.SHIFT_RIGHT_CLICK)
                ),
                Arrays.asList(
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.TOTEM_OF_UNDYING),
                        new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.NETHERITE_INGOT),
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.BREEZE_ROD), new ItemStack(Material.TOTEM_OF_UNDYING)
                ), true));
        ItemRegistry.registerItem(new FireGlaxRelement(Material.PAPER, 1, legacySection.serialize(fireGlaxRelementName), this,
                List.of(
                        new ItemAbility("e", List.of("#e"), ActionType.RIGHT_CLICK),
                        new ItemAbility("e", List.of("#e"), ActionType.SHIFT_RIGHT_CLICK)
                )));
        ItemRegistry.registerItem(new TestLevelableItem(Material.PAPER, 1, legacySection.serialize(retrixsRelementName), this,
                List.of(
                        new ItemAbility("e", List.of("#e"), ActionType.RIGHT_CLICK),
                        new ItemAbility("e", List.of("#e"), ActionType.SHIFT_RIGHT_CLICK)
                ),
                Arrays.asList(
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.TOTEM_OF_UNDYING),
                        new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.DRAGON_EGG), new ItemStack(Material.NETHERITE_INGOT),
                        new ItemStack(Material.TOTEM_OF_UNDYING), new ItemStack(Material.BREEZE_ROD), new ItemStack(Material.TOTEM_OF_UNDYING)
                ), true));
        ItemRegistry.registerItem(new DragonBlade(Material.NETHERITE_SWORD, 1, "e", this,
                List.of(
                        new ItemAbility("e", List.of("#e"), ActionType.RIGHT_CLICK),
                        new ItemAbility("e", List.of("#e"), ActionType.SHIFT_RIGHT_CLICK)
                )));
    }

    @SuppressWarnings("all")
    private void registerCommands() {
        getCommand("getitemlevel").setExecutor(new GetItemLevel());
        getCommand("setitemlevel").setExecutor(new SetItemLevel());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
