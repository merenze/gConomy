package codes.jellyrekt.gconomy.cmd;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.yaml.Messages;

public class EconomyCommand extends gConomyCommandExecutor {
	public EconomyCommand(gConomy plugin) {
		super(plugin, "economy");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ArrayList<String> msg = new ArrayList<>(Messages.getList("cmd.economy"));
		sender.sendMessage(msg.toArray(new String[msg.size()]));
		return true;
	}
}
