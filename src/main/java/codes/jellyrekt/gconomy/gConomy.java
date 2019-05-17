package codes.jellyrekt.gconomy;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.cmd.*;
import codes.jellyrekt.gconomy.util.yaml.Balances;
import codes.jellyrekt.gconomy.util.yaml.Messages;

public class gConomy extends JavaPlugin {
	/**
	 * Handle to the running instance of this plugin.
	 */
	private static gConomy instance;
	
	@Override
	public void onEnable() {
		instance = this;
		reloadConfig();
		try {
			loadFiles();
		} catch (IOException ex) {
			getLogger().info(ChatColor.RED + "Failed to create one or more data files. Disabling.");
			getServer().getPluginManager().disablePlugin(this);
		}
		registerCommands();
	}
	
	private void registerCommands() {
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand("buy").setExecutor(new BuyCommand(this));
		getCommand("sell").setExecutor(new SellCommand(this));
		getCommand("deposit").setExecutor(new DepositCommand(this));
		getCommand("withdraw").setExecutor(new WithdrawCommand(this));
		getCommand("price").setExecutor(new PriceCommand(this));
	}
	
	private void loadFiles() throws IOException {
		saveResource("message-config.yml", false);
		Messages.load(this, "messages");
		Balances.load(this, "balances");
	}
	/**
	 * Return the handle to the running instance of this plugin.
	 * @return instance of gConomy
	 */
	public static gConomy instance() {
		return instance;
	}
}
