package codes.jellyrekt.gconomy.cmd;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Messages;
import codes.jellyrekt.gconomy.util.SalesLog;
import codes.jellyrekt.gconomy.util.Transaction;

public class BuyCommand extends gConomyCommandExecutor {

	public BuyCommand(gConomy plugin) {
		super(plugin, "buy");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("must-be-player"));
			return true;
		}
		if (args.length < 3) {
			displayUsage(sender);
			return true;
		}
		int amount;
		try {
			amount = parseAmount(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		Material material = parseMaterial(args);
		if (material == null) {
			displayUsage(sender);
			return true;
		}
		double totalPrice;
		try {
			totalPrice = parsePrice(args);
		} catch (NumberFormatException ex) {
			displayUsage(sender);
			return true;
		}
		Player player = (Player) sender;
		HashSet<Transaction> result = SalesLog.makeTransaction(player, material, amount, totalPrice);
		if (result == null) {
			sender.sendMessage(Messages.get("cmd.internal-failure"));
			return true;
		}
		double totalSpent = 0.0;
		for (Transaction t : result)
			totalSpent += t.total();
		
		return true;
	}
	
}
