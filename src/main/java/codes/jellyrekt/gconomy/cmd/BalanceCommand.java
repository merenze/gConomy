package codes.jellyrekt.gconomy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.Balances;
import codes.jellyrekt.gconomy.util.Messages;

/**
 * Executor for /balance
 * 
 * @author JellyRekt
 *
 */
public class BalanceCommand extends gConomyCommandExecutor {

	public BalanceCommand(gConomy plugin) {
		super(plugin, "balance");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Player check.
		if (!(sender instanceof Player)) {
			displayUsage(sender);
			sender.sendMessage(Messages.get("cmd.must-be-player"));
			return true;
		}
		// Send player balance message.
		Player player = (Player) sender;
		sender.sendMessage(
				Messages.get("cmd.balance.success").replaceAll("%AMOUNT%", "" + Balances.getBalance(player)));
		return true;
	}

}
