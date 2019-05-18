package codes.jellyrekt.gconomy.cmd;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Messages;
import codes.jellyrekt.gconomy.util.Sale;
import codes.jellyrekt.gconomy.util.SalesLog;
/**
 * Executor for /sell
 * @author JellyRekt
 *
 */
public class SellCommand extends gConomyCommandExecutor {

	public SellCommand(gConomy plugin) {
		super(plugin, "cmd.sell");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Player check
		if (!(sender instanceof Player)) {
			displayUsage(sender);
			sender.sendMessage(Messages.get("must-be-player"));
			return true;
		}
		// Arg length check
		if (args.length < 3) {
			displayUsage(sender);
			return true;
		}
		// Parse material
		Material material = parseMaterial(args);
		if (material == null) {
			displayUsage(sender);
			return true;
		}
		// Parse amount
		int amount;
		try {
			amount = parseAmount(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		// Parse price
		double price;
		try {
			price = parsePrice(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		Player player = (Player) sender;
		if (amount > amountInInventory(player, material)) {
			sender.sendMessage(Messages.get("cmd.not-enough-in-inventory").replaceAll("%AMOUNT%", "" + amount)
					.replaceAll("%MATERIAL%", material.toString()));
			return true;
		}
		if (amount <= 0) {
			sender.sendMessage(Messages.get(key() + ".amount-zero"));
			return true;
		}
		Sale sale = new Sale(player, material, amount, price);
			if (SalesLog.log(sale))
				sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount)
						.replaceAll("%TOTAL%", "" + price).replaceAll("%PRICE%", "" + sale.price())
						.replaceAll("%MATERIAL%", material.toString()));
			else
				sendError(sender);
		removeFromInventory(player, material, amount);
		return true;
	}
}
