package me.foxils.rezineSMP.commands;

import me.foxils.foxutils.Item;
import me.foxils.foxutils.registry.ItemRegistry;
import me.foxils.rezineSMP.items.RelementItem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GetItemLevel implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Sender is not an player");
            return true;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        final Item item = ItemRegistry.getItemFromItemStack(itemStack);

        if (!(item instanceof RelementItem)) {
            commandSender.sendMessage(ChatColor.YELLOW + "Held item is not an ArtifactItem");
            return true;
        }

        commandSender.sendMessage(item.getName() + "'s" + ChatColor.RESET + " Level: " + RelementItem.getItemStackLevel(itemStack));
        return true;
    }
}