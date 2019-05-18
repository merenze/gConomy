package codes.jellyrekt.gconomy.cmd;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Messages;

/**
 * Parent class for all gConomy command executors.
 * 
 * @author JellyRekt
 */
public abstract class gConomyCommandExecutor implements CommandExecutor {
	/**
	 * Running instance of gConomy plugin.
	 */
	private gConomy plugin;
	/**
	 * YAML-friendly path to messages associated with this command.
	 */
	private String key;

	private gConomyCommandExecutor(gConomy plugin) {
		this.plugin = plugin;
	}

	/**
	 * Construct an Executor for the given plugin
	 * 
	 * @param plugin  Running instance of gConomy.
	 * @param command Name of command.
	 */
	protected gConomyCommandExecutor(gConomy plugin, String command) {
		this(plugin);
		key = "cmd." + command;
	}

	/**
	 * Get the running instance of gConomy.
	 * 
	 * @return Running instance of gConomy.
	 */
	protected gConomy plugin() {
		return plugin;
	}

	/**
	 * Get YAML-friendly path to messages associated with this command.
	 * 
	 * @return "cmd.name"
	 */
	protected String key() {
		return key;
	}

	/**
	 * Parse the amount of material specified in this command.
	 * 
	 * @param args Command arguments.
	 * @return Integer form of arg[0].
	 * @throws NumberFormatException if arg[0] cannot be parsed as an int.
	 */
	protected static int parseAmount(String[] args) throws NumberFormatException {
		return Integer.parseInt(args[0]);
	}

	/**
	 * Parse the material specified in this command.
	 * 
	 * @param args Command arguments.
	 * @return Material specified by arg[1], or null if arg[1] is an invalid
	 *         material name.
	 */
	@SuppressWarnings("deprecation")
	protected static Material parseMaterial(String[] args) {
		Material result;
		try {
			result = Material.getMaterial(Integer.parseInt(args[1]));
		} catch (NumberFormatException ex) {
			result = Material.getMaterial(args[1].toUpperCase());
		}
		return result;
	}

	/**
	 * Parse the price specified in this command.
	 * 
	 * @param args Command arguments.
	 * @return Double form of arg[2].
	 * @throws NumberFormatException if arg[2] cannot be parsed as a double.
	 */
	protected static double parsePrice(String[] args) throws NumberFormatException {
		return Double.parseDouble(args[2]);
	}

	/**
	 * Display the usage message associated with this command.
	 * 
	 * @param sender CommandSender receiving the message.
	 */
	protected void displayUsage(CommandSender sender) {
		sender.sendMessage(Messages.get(key + ".usage"));
	}

	/**
	 * Display the general player-friendly error message for this plugin.
	 * 
	 * @param sender CommandSender receiving the message.
	 */
	protected void sendError(CommandSender sender) {
		sender.sendMessage(Messages.get("cmd.internal-failure"));
	}

	/**
	 * Remove Materials from a Player Inventory.
	 * 
	 * @param player   Target Player.
	 * @param material Target Material.
	 * @param amount   Amount of Material to remove.
	 */
	protected void removeFromInventory(Player player, Material material, int amount) {
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

	/**
	 * Get the amount of given Material in a Player Inventory.
	 * 
	 * @param player   Target Player.
	 * @param material Target Material.
	 * @return
	 */
	protected int amountInInventory(Player player, Material material) {
		int result = 0;
		for (ItemStack item : player.getInventory())
			// Don't count null slots, wrong item, or damaged items
			if (item != null && item.getType() == material && item.getDurability() == material.getMaxDurability())
				result += item.getAmount();
		return result;
	}
}
