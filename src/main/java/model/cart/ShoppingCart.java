package model.cart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.product.Item;
/**
 * ShoppingCart represents a user's cart that holds multiple items (CartItem) for purchase.
 * It provides functionalities to add, update, remove, clear, and calculate the total price.
 */
public class ShoppingCart {
	// private String cartId;
	private List<CartItem> items;// List of items in the cart
	private double total;// Total price of all items in the cart

	public double getTotal() {
		return total;
	}
	 /**
     * Constructs an empty shopping cart.
     */
	public ShoppingCart() {
		// this.cartId = UUID.randomUUID().toString();
		this.items = new ArrayList<>();
		this.total = getTotalPrice();
	}


	  /**
     * Adds a new item to the shopping cart.
     * - If the item already exists, its quantity is updated.
     * - If the item is new, it is added to the cart.
     *
     * @param item The CartItem to add or update in the cart.
     */
	public void addItem(CartItem item) {
		CartItem temp = searchByItem(item);
		if (temp != null) {
			temp.setQuantity(temp.getQuantity() + item.getQuantity());
		} else {
			temp = new CartItem(item);
			this.items.add(temp);
		}

	}

	/**
     * Updates the quantity of a specific item in the cart.
     *
     * @param item   The CartItem to update.
     * @param newQty The new quantity to set.
     * @throws IllegalArgumentException If the item is not found in the cart.
     */
	public void update(CartItem item, int newQty) {
		CartItem temp = searchByItem(item);
		if (temp != null) {
			temp.setQuantity(newQty);
		} else {
			throw new IllegalArgumentException("Item not found in the cart");
		}
	}

	/**
     * Removes a specific CartItem from the cart.
     *
     * @param item The CartItem to remove.
     */
	public void remove(CartItem item) {
		Iterator<CartItem> iter = items.iterator();
		while (iter.hasNext()) {
			CartItem temp = iter.next();
			if (temp.getItem().getItemId() == item.getItem().getItemId()) {
				iter.remove(); // Remove the item with the matching product id
				break; // Exit loop after removing the item
			}

		}
	}
	/**
     * Removes a specific item from the cart based on the Item object.
     *
     * @param item The Item to remove.
     */
	public void remove(Item item) {
		Iterator<CartItem> iter = items.iterator();
		while (iter.hasNext()) {
			CartItem temp = iter.next();
			if (temp.getItem().getItemId() == item.getItemId()) {
				iter.remove(); // Remove the item with the matching product id
				break; // Exit loop after removing the item
			}

		}
	}
	/**
     * Calculates the total price of all items in the cart.
     *
     * @return The total price of all items.
     */
	public double getTotalPrice() {
		double total = 0;
		for (CartItem item : items) {
			total += item.getPriceAtPurchase();
		}
		return total;
	}
	/**
     * Clears all items from the shopping cart.
     */
	public void clearCart() {
		items.clear();
	}
	/**
     * Retrieves the list of all items in the cart.
     *
     * @return A list of CartItem objects in the cart.
     */
	public List<CartItem> getItems() {
		return items;
	}

    /**
     * Searches for a CartItem in the cart that matches the given item.
     *
     * @param item The CartItem to search for.
     * @return The matching CartItem if found, or null if not.
     */
	private CartItem searchByItem(CartItem item) {
		for (CartItem i : this.items) {
			if (item.getItem().getItemId() == i.getItem().getItemId()) {
				return i;
			}
		}
		return null; // return null if item is not found

	}
}