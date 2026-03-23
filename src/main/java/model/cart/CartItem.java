package model.cart;

import model.product.Item;
/**
 * CartItem represents an item added to the shopping cart.
 * It includes the item details, quantity, and price at the time of purchase.
 */
public class CartItem {
	private Item item;// The product item
	private int quantity;// Quantity of the product in the cart
	private double priceAtPurchase;// Total price of the item at purchase (unit price * quantity)
    /**
     * Constructs a CartItem with a given product and quantity.
     *
     * @param product  The Item object representing the product.
     * @param quantity The quantity of the product in the cart.
     */

	public CartItem(Item product, int quantity) {

		this.item = product;
		this.quantity = quantity;
		this.priceAtPurchase = getPriceAtPurchase();
	}
	 /**
     * Constructs a new CartItem by copying the details from an existing CartItem.
     *
     * @param item The CartItem to copy from.
     */
	public CartItem(CartItem item) {

		this.item = item.item;
		this.quantity = item.quantity;
		this.priceAtPurchase = getPriceAtPurchase();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPriceAtPurchase() {
		return item.getPrice() * quantity;
	}

	public String getCartItemDetail() {
		return item.getItemDetails();
	}

	public Item getItem() {
		// TODO Auto-generated method stub
		return this.item;
	}
}
