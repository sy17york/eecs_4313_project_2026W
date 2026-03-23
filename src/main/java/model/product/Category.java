package model.product;
/**
 * Category represents a product category that groups related items.
 * It includes a unique identifier and a description for the category.
 */
public class Category {
	 private Long id;                     // Unique identifier for the category
	 private String categoryDescription;  // Description of the product category
	 /**
	 * Default constructor that initializes an empty Category object.
	 */
	public Category() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	@Override
	public String toString() {
		return "Category - Id: " + id + ", Category Description: " + categoryDescription;
	}

}
