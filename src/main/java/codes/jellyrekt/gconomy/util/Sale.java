package codes.jellyrekt.gconomy.util;

import java.util.UUID;

import org.bukkit.entity.Player;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.util.yaml.SalesLog;

import org.bukkit.Material;

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
	 * Price of the material as set by the seller.
	 */
	private double price;

	/**
	 * Create a new Sale.
	 * 
	 * @param seller
	 * @param material
	 * @param price
	 */
	private Sale(Player seller, Material material, double price) {
		this(UUID.randomUUID(), seller, material, price);
	}
	/**
	 * Get a sale by its key. Only called by Sales.get().
	 * @param key Unique identifier for sale.
	 */
	public Sale(UUID key , Player seller, Material material, double price) {
		this.key = key;
		this.seller = seller;
		this.material = material;
		this.price = price;
	}

	public UUID getKey() {
		return key;
	}

	public Player getSeller() {
		return seller;
	}

	public Material getMaterial() {
		return material;
	}
	
	public double getPrice() {
		return price;
	}
	/**
	 * Add sales to the log.
	 * 
	 * @param seller
	 * @param material
	 * @param amount
	 * @param price
	 */
	public static void add(Player seller, Material material, int amount, double price) {
		SalesLog log = gConomy.instance().salesLog();
		for (int i = 0; i < amount; i++)
			log.add(new Sale(seller, material, price / amount));
	}

}
