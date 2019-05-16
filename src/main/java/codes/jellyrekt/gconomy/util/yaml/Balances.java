package codes.jellyrekt.gconomy.util.yaml;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Balances extends CustomConfig {
	public Balances(JavaPlugin plugin, String filename) throws IOException {
		super(plugin, filename);
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
	 * Set's a player's balance.
	 * @param player
	 * @param amount
	 */
	private void setBalance(Player player, double amount) {
		getYaml().set(player.getUniqueId().toString(), amount);
	}
	/**
	 * Adds the given amount to a player's balance. Accepts negative values.
	 * @param player
	 * @param amount
	 */
	public void add(Player player, double amount) {
		setBalance(player, getBalance(player) + amount);
	}
}
