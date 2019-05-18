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
 * Executor for /withdraw
 * @author JellyRekt
 *
 */
public class WithdrawCommand extends gConomyCommandExecutor {	
	public WithdrawCommand(gConomy plugin) {
		super(plugin, "withdraw");
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
		if (args.length <= 0) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		// Valid amount check
		int amount;
		try {
			amount = parseAmount(args);
		} catch (NumberFormatException ex) {
			// Check for "all" argument
			if (!args[0].equalsIgnoreCase("all")) {
				displayUsage(sender);
				return true;
			}
			amount = (int) Balances.getBalance(player);
		}
		amount = Math.max(amount, 0);
		amount = Math.min(amount, (int) Balances.getBalance(player));
		// Player balance update
		Balances.add(player, -amount);
		// Player Inventory update
		player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amount));
		// Player success message
		sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount));
		return true;
	}
}
