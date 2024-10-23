package me.foxils.rezineSMP.items;

import me.foxils.foxutils.Item;
import me.foxils.foxutils.utilities.ItemAbility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ArtifactItem extends Item {

    private int minimumLevel;
    private int maximumLevel;

    public ArtifactItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, List<ItemStack> itemsForRecipe, boolean shapedRecipe, int minimumLevel, int maximumLevel) {
        super(material, customModelData, name, plugin, abilityList, itemsForRecipe, shapedRecipe);

        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    public ArtifactItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, int minimumLevel, int maximumLevel) {
        this(material, customModelData, name, plugin, abilityList, null, false, minimumLevel, maximumLevel);
    }

    public void setMinimumLevel(int minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public void setMaximumLevel(int maximumLevel) {
        this.maximumLevel = maximumLevel;
    }

    public int getMinimumLevel() {
        return this.minimumLevel;
    }

    public int getMaximumLevel() {
        return this.maximumLevel;
    }

}
