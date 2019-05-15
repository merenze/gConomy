package codes.jellyrekt.gconomy.util;

import java.util.HashSet;

import org.bukkit.entity.Player;

public class Transaction {
	private Player buyer;
	private HashSet<Sale> sales;
	
	public Transaction(Player buyer) {
		this.buyer = buyer;
		sales = new HashSet<>();
	}
	
	public void addSale(Sale sale) {
		sales.add(sale);
	}
	
	public Player getBuyer() {
		return buyer;
	}
	
	public Player getSeller() {
		for (Sale sale : sales)
			return sale.getSeller();
		return null;
	}
	public double getAmount() {
		return sales.size();
	}
	
	public double getTotalCost() {
		double result = 0.0;
		for (Sale sale : sales)
			result += sale.getPrice();
		return result;
	}
}
