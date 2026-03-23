<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>User Profile</title>
<link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
	<h1>User Profile</h1>
 <!-- Display Error Message -->
	<c:if test="${not empty error}">
		<div style="color: red; font-weight: bold;">${error}</div>
	</c:if>

	

	 <!-- Personal Information Section -->
    <div id="personal" class="section active">
        <h2>Personal Information   <button onclick="showSection('personal')">modify</button></h2>
        <p><strong>Name:</strong> ${user.username}</p>
        <p><strong>Email:</strong> ${user.email}</p>
    </div>
	<br>
    <!-- Address Section -->
    <div id="address" class="section">
        <h2>Address   <button onclick="showSection('address')">modify</button></h2>
        <p><strong>Street:</strong> ${address.street}</p>
        <p>
		    <strong>Province:</strong> ${address.province} <br>
		    <strong>Country:</strong> ${address.country}
		</p>
        <p><strong>ZIP:</strong> ${address.zip}</p>
        <p><strong>Phone:</strong> ${address.phone}
    </div>
	<br>
    <!-- Payment Section -->
    <div id="payment" class="section">
        <h2>Payment   <button onclick="showSection('payment')">modify</button></h2>
        <p><strong>Card Number:</strong> ${payment.card_number}</p>
    </div>
	<br>
	<!-- Form Sections -->
	 <!-- Personal Information Form -->
	<form action="UserController" method="post" id="personalSection"
		style="display: none;">
		<input type="hidden" name="action" value="updatePersonal">
		<h3>Personal Information</h3>
		<label for="name">Name:</label> <input type="text" name="name"
			value="${user.username}" required> <label for="email">Email:</label>
		<input type="email" name="email" value="${user.email}" required>

		<label for="password">Password:</label> <input type="password"
			name="password" placeholder="New Password (optional)">

		<button type="submit">Update Personal Information</button>
	</form>
	 <!-- Address Update Form -->
	<form action="UserController" method="post" id="addressSection"
		style="display: none;">
		<input type="hidden" name="action" value="updateAddress">
		<h3>Address Information</h3>
		<label for="street">Street:</label> <input type="text" name="street"
			value="${address.street}" required> <label for="province">Province:</label>
		<input type="text" name="province" value="${address.province}"
			required> <label for="country">Country:</label> <input
			type="text" name="country" value="${address.country}" required>

		<label for="zip">ZIP Code:</label> <input type="text" name="zip"
			value="${address.zip}" required> <label for="phone">Phone:</label>
		<input type="text" name="phone" value="${address.phone}" required>

		<button type="submit">Update Address Information</button>
	</form>
	<!-- Payment Update Form -->
	<form action="UserController" method="post" id="paymentSection"
		style="display: none;">
		<input type="hidden" name="action" value="updatePayment">
		<h3>Payment Information</h3>
		<label for="card_number">Card Number:</label> <input type="text"
			name="card_number" value="${payment.card_number}" required> <label
			for="pin">PIN:</label> <input type="text" name="pin"
			value="${payment.pin}" required>

		<button type="submit">Update Payment Information</button>
	</form>
	<hr>
		
		<br>
		
    		<c:if test="${not empty sales}">
	            <h2>My Order History</h2>
		            <table>
		                <tr>
		                    <th>Order ID</th>
		                    <th>Date</th>
		                    <th>Total</th>
		                    <th>Actions</th>
		                </tr>
		                <c:forEach var="order" items="${sales}">
		                    <tr>
		                        <td>${order.orderId}</td>
							<td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							
							<td>${order.totalAmount}</td>
							<td><a href="UserController?action=viewOrderDetails&orderId=${order.orderId}">View Details</a>
		                        </td>
							</tr>
						</c:forEach>
					</table>
				</c:if>    

		<c:if test="${not empty orderDetails}">
		<a href="<%= request.getContextPath() %>/UserController?action=profile" class="button">back to Order History</a>
		    <h2>Order Details</h2>
		    
			<p>
				<strong>Order ID:</strong> ${orderDetails.orderId}
			</p>
			<p>
				<strong>Customer:</strong> ${orderDetails.customer.username}
				(${orderDetails.customer.email})
			</p>
			<p>
				<strong>Date:</strong> <fmt:formatDate value="${orderDetails.orderDate}" pattern="yyyy-MM-dd HH:mm:ss" />
			</p>
			<p>
				<strong>Total Amount:</strong> ${orderDetails.totalAmount}
			</p>
			<p>
				<strong>Status:</strong> ${orderDetails.status}
			</p>
	
			<h3>Shipping Address</h3>
			<p>
				${orderDetails.address.street}, ${orderDetails.address.province},
				${orderDetails.address.country}<br> ZIP: ${orderDetails.address.zip}<br>
				Phone: ${orderDetails.address.phone}
			</p>
	
			<h3>Items</h3>
			<table border="1">
				<tr>
					<th>Item Name</th>
					<th>Description</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Total</th>
				</tr>
				<c:forEach var="item" items="${orderDetails.items.items}">
					<tr>
						<td>${item.item.name}</td>
						<td>${item.item.description}</td>
						<td>${item.item.price}</td>
						<td>${item.quantity}</td>
						<td>${item.item.price * item.quantity}</td>
					</tr>
				</c:forEach>
			</table>
			
		</c:if>
	
	
	<c:if test="${not empty sessionScope.profileSuccessMessage}">
    <div style="color: green; font-weight: bold;">
        ${sessionScope.profileSuccessMessage}
    </div>
    <c:remove var="profileSuccessMessage" scope="session" />
</c:if>
	<script>
    // JavaScript to toggle visibility of sections
    function showSection(sectionId) {
        // Hide all sections
        document.getElementById('personalSection').style.display = 'none';
        document.getElementById('addressSection').style.display = 'none';
        document.getElementById('paymentSection').style.display = 'none';
     

        // Show selected section
        document.getElementById(sectionId + 'Section').style.display = 'block';
        
      
    }
</script>
<a href="<%= request.getContextPath() %>/ItemController" class="button">back to home</a>
</body>
</html>
