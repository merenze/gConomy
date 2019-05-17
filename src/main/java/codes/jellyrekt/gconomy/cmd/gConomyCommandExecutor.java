package codes.jellyrekt.gconomy.cmd;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Messages;

public abstract class gConomyCommandExecutor implements CommandExecutor {
	private gConomy plugin;
	private String key;

	private gConomyCommandExecutor(gConomy plugin) {
		this.plugin = plugin;
	}

	/**
	 * Construct an Executor for the given plugin
	 * 
	 * @param plugin  Running instance of gConomy
	 * @param command Name of command
	 */
	protected gConomyCommandExecutor(gConomy plugin, String command) {
		this(plugin);
		key = "cmd." + command;
	}

	protected gConomy plugin() {
		return plugin;
	}

	protected void sendUsage(CommandSender sender) {
		sender.sendMessage(Messages.get(key + ".usage"));
	}

	protected String key() {
		return key;
	}

	protected static int parseAmount(String[] args) throws NumberFormatException {
		return Integer.parseInt(args[0]);
	}

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

	protected static double parsePrice(String[] args) throws NumberFormatException {
		return Double.parseDouble(args[2]);
	}
	
	protected void displayUsage(CommandSender sender) {
		sender.sendMessage(Messages.get(key + ".usage"));
	}
	
	protected void sendError(CommandSender sender) {
		sender.sendMessage(Messages.get("cmd.internal-failure"));
	}
}
