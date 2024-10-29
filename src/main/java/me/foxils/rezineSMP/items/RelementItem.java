package me.foxils.rezineSMP.items;

import me.foxils.foxutils.Item;
import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.foxutils.utilities.ItemAbility;
import me.foxils.foxutils.utilities.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

@SuppressWarnings("unused")
public abstract class RelementItem extends Item {

    private final NamespacedKey ARTIFACT_ITEM_LEVEL_KEY;
    private final NamespacedKey ABLITIES_ENABLED_KEY;

    private int minimumLevel;
    private int maximumLevel;

    public RelementItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, List<ItemStack> itemsForRecipe, boolean shapedRecipe, int minimumLevel, int maximumLevel) {
        super(material, customModelData, name, plugin, abilityList, itemsForRecipe, shapedRecipe);

        ARTIFACT_ITEM_LEVEL_KEY = new NamespacedKey(plugin, "artifact_level");
        ABLITIES_ENABLED_KEY = new NamespacedKey(plugin, "abilities_enabled");

        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    public RelementItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, int minimumLevel, int maximumLevel) {
        this(material, customModelData, name, plugin, abilityList, null, false, minimumLevel, maximumLevel);
    }

    @Override
    public ItemStack createItem(int amount) {
        ItemStack newItem = super.createItem(amount);

        if (!setItemStackLevel(newItem, minimumLevel)) {
            plugin.getLogger().severe("Error creating ItemStack for " + getClass().getName() + " item class: Cannot set " + ARTIFACT_ITEM_LEVEL_KEY);
        }

        if (!setAbilitiesEnabled(newItem, true)) {
            plugin.getLogger().severe("Error creating ItemStack for " + getClass().getName() + " item class: Cannot set " + ABLITIES_ENABLED_KEY);
        }

        return newItem;
    }

    public static boolean getAbilitiesEnabled(ItemStack artifactItemStack) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItemStack) instanceof RelementItem relementItem)) return false;

        final Boolean abilitiesEnabled = ItemUtils.getDataOfType(PersistentDataType.BOOLEAN, relementItem.ABLITIES_ENABLED_KEY, artifactItemStack);

        if (abilitiesEnabled == null) return false;

        return abilitiesEnabled;
    }

    public static boolean setAbilitiesEnabled(ItemStack artifactItemStack, boolean abilitiesEnabled) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItemStack) instanceof RelementItem relementItem)) return false;

        ItemUtils.storeDataOfType(PersistentDataType.BOOLEAN, abilitiesEnabled, relementItem.ABLITIES_ENABLED_KEY, artifactItemStack);
        return true;
    }

    public static int getItemStackLevel(ItemStack artifactItemStack) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItemStack) instanceof RelementItem relementItem)) {
            System.out.println("e");
            return 0;
        }

        if (!getAbilitiesEnabled(artifactItemStack)) {
            System.out.println("2");
            return relementItem.getMinimumLevel() - 1;
        }

        return ItemUtils.getIntegerDataFromWeaponKey(relementItem.ARTIFACT_ITEM_LEVEL_KEY, artifactItemStack);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean setItemStackLevel(ItemStack artifactItemStack, int level) {
        if (!(ItemRegistry.getItemFromItemStack(artifactItemStack) instanceof RelementItem relementItem)) return false;

        if (level > relementItem.getMaximumLevel() || level < relementItem.getMinimumLevel()) return false;

        ItemUtils.storeIntegerData(relementItem.ARTIFACT_ITEM_LEVEL_KEY, artifactItemStack, level);

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
