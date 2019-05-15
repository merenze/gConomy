package codes.jellyrekt.gconomy.util.yaml;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import codes.jellyrekt.gconomy.gConomy;
import codes.jellyrekt.gconomy.exception.SaleNotDefinedException;
import codes.jellyrekt.gconomy.util.Sale;
import codes.jellyrekt.gconomy.util.Transaction;

public class SalesLog extends CustomConfig {
	public SalesLog(JavaPlugin plugin, File file) {
		super(plugin, file);
	}
	
	public void add(Sale sale) {
		YamlConfiguration config = getYaml();
		String key = sale.getKey() + ".";
		config.set(key + "seller", sale.getSeller().getUniqueId().toString());
		config.set(key + "material", sale.getMaterial().toString());
		config.set(key + "price", sale.getPrice());
	}
	
	public HashMap<Player, Transaction> makePurchase(Player buyer, Material material, int amount, double price) {
		Balances balances = gConomy.instance.balances();
		// The tricky part of this method is designing it so
		// a) The buyer gets the least expensive products on the market
		// b) When messaging players, the sellers do not receive repeat messages.
		// To accomplish this, we have the Transaction object, which is a wrapper for a list of sales.
		// Each seller associated in this purchase has his own transaction, so he need receive only one
		// message, telling him the amount sold and price received.
		Stack<Sale> sales = get(material);
		HashMap<Player, Transaction> transactions = new HashMap<>();
		while (amount-- > 0 && sales.size() > 0) {
			Sale sale = sales.pop();
			Player seller = sale.getSeller();
			if (!transactions.containsKey(seller))
				transactions.put(seller, new Transaction(buyer));
			transactions.get(seller).addSale(sale);
		}
		// Get the total cost paid (which may be different than the price offered by the buyer)
		double totalCost = 0.0;
		for (Iterator<Entry<Player, Transaction>> iter = transactions.entrySet().iterator(); iter.hasNext();) 
			totalCost += transactions.get(iter.next().getKey()).getTotalCost();
		// Update buyer balance
		balances.add(buyer, -totalCost);
		// Update seller balances
		for (Iterator<Entry<Player, Transaction>> iter = transactions.entrySet().iterator(); iter.hasNext();) {
			Transaction transaction = transactions.get(iter.next().getKey());
			balances.add(transaction.getSeller(), transaction.getTotalCost());
		}
		return transactions;
	}
	
	private Stack<Sale> get(Material material) {
		String mat = material.toString();
		Stack<Sale> sales = new Stack<>();
		YamlConfiguration config = getYaml();
		// Get all sales matching the material
		for (String key : config.getConfigurationSection("").getKeys(false))
			if (config.getString(key + "." + "material").equals(mat))
				sales.push(new Sale(key));
		// Sort sales by price, high to low
		Comparator<Sale> comparator = new Comparator<Sale>() {
			@Override
			public int compare(Sale s1, Sale s2) {
				if (s1.getPrice() > s2.getPrice())
					return -1;
				if (s1.getPrice() < s2.getPrice())
					return 1;
				return 0;
			}
		};
		sales.sort(comparator);
		return sales;
	}
	
	private Sale get(String key) throws SaleNotDefinedException {
		// Check for key's existence in log
		if (!getYaml().getConfigurationSection("").getKeys(false).contains(key))
			throw new SaleNotDefinedException("No sale logged with key " + key);
		return new Sale(key);
	}
}
