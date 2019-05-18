package codes.jellyrekt.gconomy.cmd;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Balances;
import codes.jellyrekt.gconomy.util.Messages;

/**
 * Executor for /deposit
 * 
 * @author JellyRekt
 *
 */
public class DepositCommand extends gConomyCommandExecutor {
	public DepositCommand(gConomy plugin) {
		super(plugin, "deposit");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Player check
		if (!(sender instanceof Player)) {
			displayUsage(sender);
			sender.sendMessage(Messages.get("cmd.must-be-player"));
			return true;
		}
		Player player = (Player) sender;
		// Arg length check
		if (args.length < 1) {
			displayUsage(sender);
			return true;
		}
		// Valid amount check
		int amount;
		try {
			// Num arg check
			amount = (int) Double.parseDouble(args[0]);
		} catch (NumberFormatException ex) {
			// "All" arg check
			if (!args[0].equalsIgnoreCase("all")) {
				sender.sendMessage(Messages.get(key() + ".usage"));
				return true;
			}
			amount = amountInInventory(player, Material.GOLD_INGOT);
		}
		amount = Math.max(amount, 0);
		amount = Math.min(amount, amountInInventory(player, Material.GOLD_INGOT));
		// Balance update
		Balances.add(player, amount);
		// Inventory update
		removeFromInventory(player, Material.GOLD_INGOT, amount);
		// Player success message
		sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount));
		return true;
	}
}
