package codes.jellyrekt.gconomy.cmd;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.yaml.Balances;
import codes.jellyrekt.gconomy.util.yaml.Messages;

public class DepositCommand extends gConomyCommandExecutor {
	public DepositCommand(gConomy plugin) {
		super(plugin, "deposit");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check for player
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("must-be-player"));
			return true;
		}
		Player player = (Player) sender;
		// Check for valid argument
		if (args.length < 1) {
			sendUsage(sender);
			return true;
		}
		int amount;
		try {
			amount = (int) Double.parseDouble(args[0]);
		} catch (NumberFormatException ex) {
			// Check for "all" argument
			if (!args[0].equalsIgnoreCase("all")) {
				sender.sendMessage(Messages.get(key() + ".usage"));
				return true;
			}
			amount = goldInInventory(player);
		}
		// Check for valid amount
		amount = Math.max(amount, 0);
		amount = Math.min(amount, goldInInventory(player));
		// Update balance
		Balances.add(player, amount);
		// Update inventory
		removeGold(player, amount);
		// Message player
		sender.sendMessage(Messages.get(key() + "success").replaceAll("%AMOUNT%", "" + amount));
		return true;
	}

	/**
	 * Get the amount of gold ingots in a Player's Inventory.
	 * 
	 * @param player
	 * @return Amount of gold ingots
	 */
	private int goldInInventory(Player player) {
		int result = 0;
		for (ItemStack item : player.getInventory())
			if (item != null && item.getType() == Material.GOLD_INGOT)
				result += item.getAmount();
		return result;
	}

	/**
	 * Remove gold ingots from a player's inventory.
	 * 
	 * @param player
	 * @param amount
	 */
	private void removeGold(Player player, int amount) {
		for (ItemStack item : player.getInventory()) {
			// Stop when the proper amount has been removed
			if (amount <= 0)
				break;
			// Check for gold ingot
			if (item.getType() == Material.GOLD_INGOT) {
				// Operate on the ItemStack until it's empty
				while (item != null && item.getAmount() > 0 && amount-- > 0)
					item.setAmount(item.getAmount() - 1);
			}
		}
	}
}
