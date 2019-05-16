package codes.jellyrekt.gconomy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.yaml.Messages;

public class BalanceCommand extends gConomyCommandExecutor {

	public BalanceCommand(gConomy plugin) {
		super(plugin, "balance");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check for player.
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin().messages().get("must-be-player"));
			return true;
		}
		// Send the player their balance.
		Player player = (Player) sender;
		player.sendMessage(plugin().messages().get("cmd.balance").replaceAll("%BALANCE%",
				"" + plugin().balances().getBalance(player)));
		// TODO
		player.sendMessage("&7" + plugin().balances().getBalance(player));
		return true;
	}

}
