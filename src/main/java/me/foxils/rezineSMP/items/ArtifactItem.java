package me.foxils.rezineSMP.items;

import me.foxils.foxutils.Item;
import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.foxutils.utilities.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

@SuppressWarnings("unused")
public abstract class ArtifactItem extends Item {

    private int minimumLevel;
    private int maximumLevel;

    private static final NamespacedKey ARTIFACT_ITEM_LEVEL_KEY = NamespacedKey.fromString("rezinesmp:artifact-level");

    public ArtifactItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, List<ItemStack> itemsForRecipe, boolean shapedRecipe, int minimumLevel, int maximumLevel) {
        super(material, customModelData, name, plugin, abilityList, itemsForRecipe, shapedRecipe);

        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    public ArtifactItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, int minimumLevel, int maximumLevel) {
        this(material, customModelData, name, plugin, abilityList, null, false, minimumLevel, maximumLevel);
    }

    @Override
    public ItemStack createItem(int amount) {
        ItemStack newItem = super.createItem(amount);

        if (!setItemStackLevel(newItem, minimumLevel)) {
            plugin.getLogger().severe("Error creating ItemStack for " + getClass().getName() + " item class: Cannot set artifact-level");
        }

        return newItem;
    }

    public static int getItemStackLevel(ItemStack artifactItem) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItem) instanceof ArtifactItem)) return 0;

        return ItemUtils.getIntegerDataFromWeaponKey(ARTIFACT_ITEM_LEVEL_KEY, artifactItem);
    }

    public static boolean setItemStackLevel(ItemStack artifactItemStack, int level) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItemStack) instanceof ArtifactItem artifactItem)) return false;

        if (level > artifactItem.getMaximumLevel() || level < artifactItem.getMinimumLevel()) return false;

        ItemUtils.storeIntegerData(ARTIFACT_ITEM_LEVEL_KEY, artifactItemStack, level);

        return true;
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
