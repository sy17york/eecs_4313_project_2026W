<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">

</head>
<body>
	<h1>Order Confirmation</h1>
	
		<!-- Display error message -->
		<c:if test="${not empty error}">
		    <div style="color: red; font-weight: bold;">${error}</div>
		</c:if>
		<!-- Order Confirmation Form -->
	<form action="OrderController" method="post">
	<!-- Address Section -->
	<div>
	    <h2>Address Selection</h2>
	    <label>
	        <input type="radio" name="addressType" value="existing" id="useExistingAddress" checked>
	        Use Existing Address
	    </label>
	    <c:if test="${address != null}">
	        <p><strong>Existing Address:</strong></p>
	        <p>${address.street}, ${address.province}, ${address.country}, ZIP: ${address.zip}</p>
	        <p>Phone: ${address.phone}</p>
	    </c:if>
	
	    <label>
	        <input type="radio" name="addressType" value="temporary" id="useTemporaryAddress">
	        Enter New Address
	    </label>
	    <div id="temporaryAddressFields" style="display: none;">
	        <input type="text" name="street" placeholder="Street">
	        <input type="text" name="province" placeholder="Province">
	        <input type="text" name="country" placeholder="Country">
	        <input type="text" name="zip" placeholder="ZIP">
	        <input type="text" name="phone" placeholder="Phone">
	    </div>
	</div>
	
	<!-- Payment Section -->
		<div>
	    <h2>Payment Selection</h2>
	    <label>
	        <input type="radio" name="paymentType" value="existing" id="useExistingPayment" checked>
	        Use Existing Payment
	    </label>
	    <c:if test="${payment != null}">
	        <p><strong>Existing Payment:</strong></p>
	        <p>Card Number: ${payment.card_number}</p>
	    </c:if>
	
	    <label>
	        <input type="radio" name="paymentType" value="temporary" id="useTemporaryPayment">
	        Enter New Payment
	    </label>
	    <div id="temporaryPaymentFields" style="display: none;">
	        <input type="text" name="card_number" placeholder="Card Number">
	        <input type="password" name="pin" placeholder="PIN">
	    </div>
		</div>
	
	<!-- Submit Button -->
		<div>
	    <button type="submit" name="action" value="placeOrder">Place Order</button>
	    <button type="button" onclick="window.location.href='ShoppingCartController?action=viewCart';">
	        Back to Cart
	    </button>
		</div>
		</form>

<script>
    // Address Selection Toggle
    document.getElementById('useTemporaryAddress').addEventListener('change', () => {
        document.getElementById('temporaryAddressFields').style.display = 'block';
    });
    document.getElementById('useExistingAddress').addEventListener('change', () => {
        document.getElementById('temporaryAddressFields').style.display = 'none';
    });

    // Payment Selection Toggle
    document.getElementById('useTemporaryPayment').addEventListener('change', () => {
        document.getElementById('temporaryPaymentFields').style.display = 'block';
    });
    document.getElementById('useExistingPayment').addEventListener('change', () => {
        document.getElementById('temporaryPaymentFields').style.display = 'none';
    });
</script>

</body>
</html>
