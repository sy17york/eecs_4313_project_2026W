package model.order;
/**
 * Payment represents a payment method used for transactions.
 * It includes details such as a unique ID, card number, and a security PIN.
 */
public class Payment {
	 private Long id;           // Unique identifier for the payment method
	 private int card_number;   // Card number associated with the payment
	 private int pin;           // Security PIN for the card

	 /**
	  * Default constructor that initializes an empty Payment object.
	  */
	public Payment() {
		super();
	}
	/**
     * Parameterized constructor to initialize a Payment object with card details.
     *
     * @param id          The unique identifier for the payment.
     * @param card_number The card number associated with the payment.
     * @param pin         The security PIN for the card.
     */
	public Payment(Long id, int card_number, int pin) {
		this.card_number = card_number;
		this.pin = pin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCard_number() {
		return card_number;
	}

	public void setCard_number(int card_number) {
		this.card_number = card_number;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}


}
