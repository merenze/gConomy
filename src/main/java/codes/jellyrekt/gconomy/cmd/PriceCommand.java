package codes.jellyrekt.gconomy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.Material;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.exception.NotEnoughOnMarketException;
import codes.jellyrekt.gconomy.util.Messages;
import codes.jellyrekt.gconomy.util.SalesLog;

public class PriceCommand extends gConomyCommandExecutor {

	public PriceCommand(gConomy plugin) {
		super(plugin, "price");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
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
		// Parse material
		Material material = parseMaterial(args);
		if (material == null) {
			displayUsage(sender);
			return true;
		}
		// Retrieve information and message sender
		try {
			sender.sendMessage(Messages.get(key() + ".success").replaceAll("%MATERIAL%", material.toString()).replaceAll("%TOTAL%", "" + SalesLog.getPrice(material, amount)).replaceAll("%AMOUNT%", "" + amount));
		} catch (NotEnoughOnMarketException ex) {
			sender.sendMessage(Messages.get("cmd.not-enough-on-market").replaceAll("%AMOUNT%", "" + amount).replaceAll("%MATERIAL%", material.toString()));
		}
		return true;
	}

}
