package codes.jellyrekt.gconomy.cmd;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.exception.NotEnoughOnMarketException;
import codes.jellyrekt.gconomy.util.Balances;
import codes.jellyrekt.gconomy.util.Messages;
import codes.jellyrekt.gconomy.util.SalesLog;
import codes.jellyrekt.gconomy.util.Transaction;
/**
 * Executor for /buy
 * @author JellyRekt
 *
 */
public class BuyCommand extends gConomyCommandExecutor {

	public BuyCommand(gConomy plugin) {
		super(plugin, "buy");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Player check
		if (!(sender instanceof Player)) {
			displayUsage(sender);
			sender.sendMessage(Messages.get("cmd.must-be-player"));
			return true;
		}
		// Arg length check
		if (args.length < 3) {
			displayUsage(sender);
			return true;
		}
		// Valid amount check
		int amount;
		try {
			amount = parseAmount(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		// Valid material check
		Material material = parseMaterial(args);
		if (material == null) {
			displayUsage(sender);
			return true;
		}
		// Valid offer check
		double offer;
		try {
			offer = parsePrice(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		// Market amount check
		Player buyer = (Player) sender;
		double realPrice;
		try {
			// Actual cost of transaction
			realPrice = SalesLog.getPrice(material, amount);
		} catch (NotEnoughOnMarketException ex) {
			sender.sendMessage(Messages.get("cmd.not-enough-on-market").replaceAll("%AMOUNT%", "" + amount)
					.replaceAll("%MATERIAL%", material.toString()));
			return true;
		}
		// Buyer balance check and offer check
		if (Balances.getBalance(buyer) < realPrice || offer < realPrice) {
			sender.sendMessage(Messages.get("cmd.not-enough-money"));
			return true;
		}
		// SalesLog update
		HashSet<Transaction> result = SalesLog.makeTransaction(buyer, material, amount, realPrice);
		if (result == null) {
			sender.sendMessage(Messages.get("cmd.internal-error"));
			return true;
		}
		// Buyer balance update
		Balances.add(buyer, -realPrice);
		// Buyer Inventory update
		buyer.getInventory().addItem(new ItemStack(material, amount));
		// Buyer completion message
		sender.sendMessage(Messages.get(key() + ".success.buyer").replaceAll("%AMOUNT%", "" + amount)
				.replaceAll("%MATERIAL%", material.toString()).replaceAll("%TOTAL%", "" + realPrice));
		for (Transaction t : result) {
			// Seller balance update
			Balances.add(t.seller(), realPrice);
			// Seller completion message
			if (Bukkit.getServer().getOnlinePlayers().contains(t.seller()))
				t.seller().sendMessage(Messages.get(key() + ".success.seller").replaceAll("%AMOUNT%", "" + amount)
						.replaceAll("%MATERIAL%", material.toString()).replaceAll("%TOTAL%", "" + realPrice));
		}
		return true;
	}

}
