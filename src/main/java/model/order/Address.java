package model.order;
/**
 * Address represents a physical address associated with an order, user, or entity.
 * It includes fields such as street, province, country, zip code, and phone number.
 */
public class Address {
	 	private Long id;           // Unique identifier for the address
	    private String street;     // Street address
	    private String province;   // Province or state
	    private String country;    // Country of the address
	    private String zip;        // ZIP or postal code
	    private String phone;      // Contact phone number

	/**
	* Default constructor that initializes an empty Address object.
	*/
	public Address() {
		super();
	}
	/**
     * Parameterized constructor that initializes an Address with all fields.
     *
     * @param id       The unique identifier of the address.
     * @param street   The street address.
     * @param province The province or state.
     * @param country  The country of the address.
     * @param zip      The ZIP or postal code.
     * @param phone    The contact phone number.
     */
	public Address(Long id, String street, String province, String country, String zip, String phone) {

		this.id = id;
		this.street = street;
		this.province = province;
		this.country = country;
		this.zip = zip;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
