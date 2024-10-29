package me.foxils.rezineSMP.items;

import me.foxils.foxutils.itemactions.ClickActions;
import me.foxils.foxutils.utilities.ItemAbility;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class TestLevelableItem extends RelementItem implements ClickActions {

    public TestLevelableItem(Material material, int customModelData, String name, Plugin plugin, List<ItemAbility> abilityList, List<ItemStack> itemsForRecipe, boolean shapedRecipe) {
        super(material, customModelData, name, plugin, abilityList, itemsForRecipe, shapedRecipe, 1, 4);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        if (getItemStackLevel(itemInteracted) < 1) return;
        event.getPlayer().sendMessage("e");
    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, ItemStack itemInteracted) {
        rightClickAir(event, itemInteracted);
    }
}
