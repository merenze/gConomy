package codes.jellyrekt.gconomy.util.yaml;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Balances extends CustomConfig {
	public Balances(JavaPlugin plugin, File file) {
		super(plugin, file);
	}
	
	/**
	 * Returns balance of player, or 0 if player is not in file.
	 * @param player
	 * @return
	 */
	public double getBalance(Player player) {
		return getYaml().getDouble(player.getUniqueId().toString());
	}
	/**
	 * 
	 * @param player
	 * @param amount
	 */
	public void setBalance(Player player, double amount) {
		getYaml().set(player.getUniqueId().toString(), amount);
	}
	/**
	 * 
	 * @param player
	 * @param amount
	 */
	public void add(Player player, double amount) {
		setBalance(player, getBalance(player) + amount);
	}
}
