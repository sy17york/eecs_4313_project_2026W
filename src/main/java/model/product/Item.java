package model.product;
/**
 * Item represents a product in the system with details such as its name,
 * description, price, stock quantity, category, and brand.
 */
public class Item {
	 	private Long itemId;          // Unique identifier for the item
	    private String name;          // Name of the item
	    private String description;   // Description of the item
	    private double price;         // Price of the item
	    private int stockQuantity;    // Quantity of the item available in stock
	    private Category category;    // Category to which the item belongs
	    private String brand;         // Brand of the item

	    /**
	     * Parameterized constructor to initialize all fields of the Item.
	     *
	     * @param itemId        The unique ID of the item.
	     * @param name          The name of the item.
	     * @param description   The description of the item.
	     * @param price         The price of the item.
	     * @param stockQuantity The quantity of the item in stock.
	     * @param category      The category to which the item belongs.
	     * @param brand         The brand of the item.
	     */

	public Item(Long itemId, String name, String description, double price, int stockQuantity, Category category,
			String brand) {
		this.itemId = itemId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.category = category;
		this.brand = brand;
	}
	 /**
     * Default constructor that initializes an empty Item object.
     */
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
     * Retrieves a detailed description of the item.
     *
     * @return A formatted string containing item details.
     */
	public String getItemDetails() {
		return "Product ID: " + itemId + ", Name: " + name + ", category: " + category.getId() + ", Description: " + description + ", Price: " + price
				+ ", Stock: " + stockQuantity;
	}

	public void updateStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long productId) {
		this.itemId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Category getCategory() {
		return category;
	}

	public Long getCategoryId() {
		return category.getId();
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
