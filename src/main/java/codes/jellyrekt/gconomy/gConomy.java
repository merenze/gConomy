package codes.jellyrekt.gconomy;

import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.cmd.*;
import codes.jellyrekt.gconomy.util.yaml.Balances;
import codes.jellyrekt.gconomy.util.yaml.CustomConfig;
import codes.jellyrekt.gconomy.util.yaml.Messages;
import codes.jellyrekt.gconomy.util.yaml.SalesLog;

public class gConomy extends JavaPlugin {
	/**
	 * Handle to the running instance of this plugin.
	 */
	private static gConomy instance;
	/**
	 * Configuration file for messages.
	 */
	private Messages messages;
	/**
	 * Flat file for storing sales.
	 */
	private SalesLog salesLog;
	/**
	 * Flat file for storing player balances.
	 */
	private Balances balances;
	@Override
	public void onEnable() {
		instance = this;
		reloadConfig();
		loadFiles();
		registerCommands();
	}
	/**
	 * Get the message fileconfiguration.
	 * @return msgConfig
	 */
	public Messages messages() {
		return messages;
	}
	/**
	 * Get the storage mechanism for the sales log.
	 * @return salesFile
	 */
	public SalesLog salesLog() {
		return salesLog;
	}
	
	/**
	 * Get the storage mechanism for balances.
	 * @return balances
	 */
	public Balances balances() {
		return balances;
	}
	
	private void registerCommands() {
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand("buy").setExecutor(new BuyCommand(this));
		getCommand("sell").setExecutor(new SellCommand(this));
		getCommand("deposit").setExecutor(new DepositCommand(this));
		getCommand("withdraw").setExecutor(new WithdrawCommand(this));
	}
	
	private void loadFiles() {
		messages = new Messages(this, "messages.yml");
		salesLog = new SalesLog(this, "sales.yml");
		balances = new Balances(this, "balances.yml");
	}
	/**
	 * Return the handle to the running instance of this plugin.
	 * @return instance of gConomy
	 */
	public static gConomy instance() {
		return instance;
	}
}
