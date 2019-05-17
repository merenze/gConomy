package codes.jellyrekt.gconomy.cmd;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Sale;
import codes.jellyrekt.gconomy.util.yaml.Messages;
import codes.jellyrekt.gconomy.util.yaml.SalesLog;

public class SellCommand extends gConomyCommandExecutor {

	public SellCommand(gConomy plugin) {
		super(plugin, "sell");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.get("must-be-player"));
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		Material material;
		try {
			material = Material.getMaterial(Integer.parseInt(args[0]));
		} catch (NumberFormatException ex) {
			material = Material.getMaterial(args[0].toUpperCase());
		}
		if (material == null) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		int amount;
		try {
			amount = (int) Double.parseDouble(args[1]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		double price;
		try {
			price = Double.parseDouble(args[2]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(Messages.get(key() + ".usage"));
			return true;
		}
		Player player = (Player) sender;
		if (amount > amountInInventory(player, material)) {
			sender.sendMessage(Messages.get(key() + ".fail").replaceAll("%AMOUNT%", "" + amount)
					.replaceAll("%MATERIAL%", material.toString()));
			return true;
		}
		if (amount <= 0) {
			sender.sendMessage(Messages.get(key() + ".amount-zero"));
			return true;
		}
		Sale sale = new Sale(player, material, amount, price);
		try {
			if (SalesLog.log(sale))
				sender.sendMessage(Messages.get(key() + ".success").replaceAll("%AMOUNT%", "" + amount)
						.replaceAll("%TOTAL%", "" + price).replaceAll("%PRICE%", "" + sale.price())
						.replaceAll("%MATERIAL%", material.toString()));
			else
				sender.sendMessage(ChatColor.DARK_RED + "Failed.");
		} catch (IOException ex) {
			sender.sendMessage(ChatColor.DARK_RED + "Failed.");
			plugin().getLogger().info(ChatColor.RED + ex.getStackTrace().toString());
		}
		removeFromInventory(player, material, amount);
		return true;
	}

	private int amountInInventory(Player player, Material material) {
		int result = 0;
		for (ItemStack item : player.getInventory())
			// Don't count null slots, wrong item, or damaged items
			if (item != null && item.getType() == material && item.getDurability() == material.getMaxDurability())
				result += item.getAmount();
		return result;
	}
	
	private void removeFromInventory(Player player, Material material, int amount) {
		for (ItemStack item : player.getInventory()) {
			if (item != null && item.getType() == material && item.getDurability() == material.getMaxDurability()) {
				int toRemove = Math.min(item.getAmount(), amount);
				item.setAmount(item.getAmount() - toRemove);
				amount -= toRemove;
			}
			if (amount <= 0)
				break;
		}
	}
}
