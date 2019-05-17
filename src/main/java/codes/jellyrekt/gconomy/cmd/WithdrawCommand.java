package codes.jellyrekt.gconomy.cmd;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Balances;
import codes.jellyrekt.gconomy.util.Messages;

public class WithdrawCommand extends gConomyCommandExecutor {	
	public WithdrawCommand(gConomy plugin) {
		super(plugin, "withdraw");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check for Player
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("must-be-player"));
			return true;
		}
		Player player = (Player) sender;
		// Check for valid argument
		if (args.length <= 0) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		int amount;
		try {
			amount = (int) Double.parseDouble(args[0]);
		} catch (NumberFormatException ex) {
			// Check for "all" argument
			if (!args[0].equalsIgnoreCase("all")) {
				sendUsage(sender);
				return true;
			}
			amount = (int) Balances.getBalance(player);
		}
		// Check for valid amount
		amount = Math.max(amount, 0);
		amount = Math.min(amount, (int) Balances.getBalance(player));
		// Update balance
		Balances.add(player, -amount);
		// Update inventory
		giveGold(player, amount);
		// Send message
		sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount));
		return true;
	}
	/**
	 * Adds gold ingots to a player's inventory.
	 * @param player
	 * @param amount
	 */
	private void giveGold(Player player, int amount) {
		HashSet<ItemStack> stacks = new HashSet<>();
		// Create the ItemStacks
		while (amount > 0) {
			ItemStack stack = new ItemStack(Material.GOLD_INGOT);
			if (amount >= 64) {
				stack.setAmount(64);
				stacks.add(stack);
				amount -= 64;
			} else {
				stack.setAmount(amount);
				stacks.add(stack);
				amount -= amount;
			}
		}
		// Add the stacks to the player's inventory
		for (ItemStack stack : stacks)
			player.getInventory().addItem(stack);
	}
}
