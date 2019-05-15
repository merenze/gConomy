package codes.jellyrekt.gconomy;

import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.cmd.*;
import codes.jellyrekt.gconomy.util.yaml.Balances;
import codes.jellyrekt.gconomy.util.yaml.CustomConfig;
import codes.jellyrekt.gconomy.util.yaml.SalesLog;

public class gConomy extends JavaPlugin {
	/**
	 * Handle to the running instance of this plugin.
	 */
	public static gConomy instance;
	/**
	 * Configuration file for messages.
	 */
	private CustomConfig msgConfig;
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
		msgConfig = CustomConfig.get(this, "messages");
		salesLog = (SalesLog) CustomConfig.get(this, "sales");
		registerCommands();
	}
	/**
	 * Get the message fileconfiguration.
	 * @return msgConfig
	 */
	public CustomConfig msgConfig() {
		return msgConfig;
	}
	/**
	 * Get the sales fileconfiguration.
	 * @return salesFile
	 */
	public SalesLog salesLog() {
		return salesLog;
	}
	
	/**
	 * Get the balances FileConfiguration.
	 * @return balances
	 */
	public Balances balances() {
		return balances;
	}
	
	private void registerCommands() {
		getCommand("economy").setExecutor(new EconomyCommand());
		getCommand("buy").setExecutor(new BuyCommand());
		getCommand("sell").setExecutor(new SellCommand());
		getCommand("deposit").setExecutor(new SellCommand());
		getCommand("withdraw").setExecutor(new SellCommand());
	}
}
