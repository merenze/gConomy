package codes.jellyrekt.gconomy.cmd;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.yaml.Messages;

public abstract class gConomyCommandExecutor implements CommandExecutor {
	private gConomy plugin;
	private String key;
	
	private gConomyCommandExecutor(gConomy plugin) {
		this.plugin = plugin;
	}
	/**
	 * Construct an Executor for the given plugin
	 * @param plugin Running instance of gConomy
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
}
