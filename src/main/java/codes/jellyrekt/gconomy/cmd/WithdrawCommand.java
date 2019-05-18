package codes.jellyrekt.gconomy.cmd;

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
			displayUsage(sender);
			sender.sendMessage(Messages.get("cmd.must-be-player"));
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
		player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amount));
		// Send message
		sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount));
		return true;
	}
}
