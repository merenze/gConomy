package codes.jellyrekt.gconomy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.exception.NotEnoughOnMarketException;
import codes.jellyrekt.gconomy.util.yaml.Messages;
import codes.jellyrekt.gconomy.util.yaml.SalesLog;

public class PriceCommand extends gConomyCommandExecutor {

	public PriceCommand(gConomy plugin) {
		super(plugin, "price");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		Material material = Material.getMaterial(args[0].toUpperCase());
		if (material == null) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		int amount;
		try {
			amount = (args.length >= 2) ? (int) Double.parseDouble(args[1]) : 1;
		} catch (NumberFormatException ex) {
			amount = 1;
		}
		try {
			sender.sendMessage(Messages.get(key() + ".success").replaceAll("%MATERIAL%", material.toString()).replaceAll("%PRICE%", "" + SalesLog.getPrice(material, amount)));
		} catch (NotEnoughOnMarketException ex) {
			sender.sendMessage(Messages.get(key() + ".fail").replaceAll("%AMOUNT%", "" + amount).replaceAll("%MATERIAL", material.toString()));
		} catch (IOException ex) {
			plugin().getLogger().info(ChatColor.RED + ex.getStackTrace().toString());
		}
		return true;
	}

}
