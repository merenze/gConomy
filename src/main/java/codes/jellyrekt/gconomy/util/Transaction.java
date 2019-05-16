package codes.jellyrekt.gconomy.util;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Transaction {
	private Player seller;
	private Material material;
	private int amount;
	private double total;
	
	public Transaction(Sale sale, int amount) {
		seller = sale.seller();
		material = sale.material();
		this.amount = amount;
		total = amount * sale.price();
	}	
	/**
	 * Seller associated with this transaction.
	 * @return
	 */
	public Player seller() {
		return seller;
	}
	/**
	 * Material associated with this transaction.
	 * @return
	 */
	public Material material() {
		return material;
	}
	/**
	 * Total earnings from this Transaction.
	 * @return
	 */
	public double total() {
		return total;
	}
	/**
	 * Amount of Material associated with this transaction.
	 */
	public int amount() {
		return amount;
	}
}