package codes.jellyrekt.gconomy.util;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.entity.Player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class Sale {
	/**
	 * Unique identifier for sale.
	 */
	private UUID key;
	/**
	 * Player selling the material.
	 */
	private Player seller;
	/**
	 * Material being sold.
	 */
	private Material material;
	/**
	 * Price per unit.
	 */
	private double price;
	/**
	 * Amount of the material being sold.
	 */
	private int amount;

	/**
	 * Create a new Sale.
	 * sto
	 * @param seller   Player selling material
	 * @param material Material being sold
	 * @param price    Total price
	 */
	public Sale(Player seller, Material material, int amount, double price) {
		this.key = UUID.randomUUID();
		this.seller = seller;
		this.material = material;
		this.amount = amount;
		this.price = price / amount;
	}

	/**
	 * Create a sale from an existing key.
	 * 
	 * @param material
	 * @param key
	 */
	private Sale(Material material, YamlConfiguration config, String key) {
		this.key = UUID.fromString(key);
		this.material = material;
		amount = config.getInt(key + ".amount");
		seller = Bukkit.getPlayer(UUID.fromString(config.getString(key + ".seller")));
		price = config.getDouble(key + ".price");
	}

	/**
	 * @return Unique id for this sale
	 */
	public UUID key() {
		return key;
	}

	/**
	 * @return Player selling this item
	 */
	public Player seller() {
		return seller;
	}

	/**
	 * @return Material being sold
	 */
	public Material material() {
		return material;
	}

	/**
	 * @return Price per item
	 */
	public double price() {
		return price;
	}

	/**
	 * @return Amount being sold
	 */
	public int amount() {
		return amount;
	}

	/**
	 * Decrements amount by the given argument.
	 * Sets amount to zero if arg > amount.
	 * 
	 * @param amount Amount to buy. Defaults to zero if < 0, or to this.amount if > this.amount.
	 * @return Actual amount removed.
	 */
	public int buyAmount(int amount) {
		int result = Math.max(0, Math.min(this.amount, amount));
		this.amount -= result;
		return result;
	}

	/**
	 * Get a sale from an existing key.
	 * 
	 * @return Existing sale
	 * @throws IOException
	 */
	public static Sale getSale(Material material, YamlConfiguration config, String key) throws IOException {
		return new Sale(material, config, key);
	}
}
