package me.foxils.rezineSMP;

import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.foxutils.utilities.ActionType;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.rezineSMP.items.TestLevelableItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class RezineSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        ItemRegistry.registerItem(new TestLevelableItem(Material.WARPED_FUNGUS_ON_A_STICK, 1, "test", this,
                List.of(
                        new ItemAbility("lol", List.of("lol"), ActionType.RIGHT_CLICK)
                ),
                Arrays.asList(
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND),
                        new ItemStack(Material.DIAMOND)
                ), true));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
