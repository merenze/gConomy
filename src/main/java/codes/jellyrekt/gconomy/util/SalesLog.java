package codes.jellyrekt.gconomy.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.exception.NotEnoughOnMarketException;

public class SalesLog extends CustomConfig {
	Material material;

	private SalesLog(JavaPlugin plugin, String dirPath, String filename) throws IOException {
		super(plugin, dirPath, filename);
		material = Material.getMaterial(filename);
	}

	/**
	 * Update the log for a player transaction.
	 * 
	 * @param buyer
	 * @param material
	 * @param amount
	 * @param totalPrice
	 * @return Actual price paid
	 * @throws IOException
	 */
	public static HashSet<Transaction> makeTransaction(Player buyer, Material material, int amount, double totalPrice) {
		SalesLog log = getLog(material);
		Stack<Sale> sales;
		try {
			sales = log.getSales();
		} catch (IOException ex) {
			gConomy.instance().getLogger().log(Level.SEVERE, "Could not access or create sales-logs\\" + material.toString() + ".yml");
			return null;
		}
		HashSet<Transaction> result = new HashSet<>();
		while (!sales.isEmpty() && totalPrice > sales.peek().price() && amount > 0) {
			// Update sale information
			Sale sale = sales.pop();
			int amountBought = sale.buyAmount(amount);
			totalPrice -= sale.price() * amountBought;
			amount -= amountBought;
			// Update file
			if (sale.amount() <= 0)
				log.getYaml().set(sale.key().toString(), null);
			else
				log(sale);
			result.add(new Transaction(sale, amountBought));
		}
		// TODO
		return result;
	}

	/**
	 * Get a Stack of Sales, ordered top to bottom from least to most expensive.
	 * 
	 * @return ordered Sales Stack
	 * @throws IOException 
	 */
	private Stack<Sale> getSales() throws IOException {
		Stack<Sale> stack = new Stack<>();
		for (String key : getYaml().getConfigurationSection("").getKeys(false))
			stack.push(Sale.getSale(material, getYaml(), key));
		Comparator<Sale> comparator = new Comparator<Sale>() {
			public int compare(Sale s1, Sale s2) {
				if (s1.price() < s2.price())
					return 1;
				if (s1.price() > s2.price())
					return -1;
				return 0;
			}
		};
		stack.sort(comparator);
		return stack;
	}
	/**
	 * Add a sale to the log.
	 * 
	 * @param sale
	 */
	private boolean addSale(Sale sale) {
		getYaml().set(sale.key() + ".seller", sale.seller().getUniqueId().toString());
		getYaml().set(sale.key() + ".price", sale.price());
		getYaml().set(sale.key() + ".amount", sale.amount());
		return save();
	}
	/**
	 * Add a Sale to its log.
	 * 
	 * de@param sale
	 * @throws IOException
	 */
	public static boolean log(Sale sale) {
		SalesLog log = getLog(sale.material());
		return log.addSale(sale);
	}

	/**
	 * Get the sales log associated with the given item.
	 * 
	 * @param Material
	 * @throws IOException
	 */
	public static SalesLog getLog(Material material) {
		try {
			return new SalesLog(gConomy.instance(), "sales-logs", material.toString());
		} catch (IOException ex) {
			gConomy.instance().getLogger().log(Level.SEVERE, "Could not access or create sales-logs\\" + material.toString() + ".yml");
			return null;
		}
	}

	/**
	 * Gets the item associated with this sales log.
	 * 
	 * @return Material item on this log
	 */
	public Material getMaterial() {
		return material;
	}
	/**
	 * Gets the market price for given amount of given material. Negative argument defaults to 0.
	 * @param material
	 * @param amount
	 * @throws IOException 
	 */
	public static double getPrice(Material material, int amount) throws NotEnoughOnMarketException {
		amount = Math.max(0, amount);
		double result = 0.0;
		Stack<Sale> sales;
		try {
			sales = getLog(material).getSales();
		} catch (IOException ex) {
			gConomy.instance().getLogger().log(Level.SEVERE, "Could not access or create sales-log" + material.toString() + ".yml");
			return -1.0;
		}
		while (amount > 0) {
			if (sales.isEmpty())
				throw new NotEnoughOnMarketException();
			Sale sale = sales.pop();
			result += Math.min(amount, sale.amount()) * sale.price();
			amount -= Math.min(amount, sale.amount());
		}
		return result;
	}
}
