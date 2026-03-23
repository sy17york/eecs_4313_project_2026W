<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Details</title>
</head>
<body>
<!-- Check if Order Exists -->
	<c:if test="${not empty order}">
		<h2>Order Details</h2>
		<!-- Order Information -->
		<p>
			<strong>Order ID:</strong> ${order.orderId}
		</p>
		<p>
			<strong>Customer:</strong> ${order.customer.username}
			(${order.customer.email})
		</p>
		<p>
			<strong>Date:</strong> ${order.orderDate}
		</p>
		<p>
			<strong>Total Amount:</strong> ${order.totalAmount}
		</p>
		<p>
			<strong>Status:</strong> ${order.status}
		</p>

 		<!-- Shipping Address -->
		<h3>Shipping Address</h3>
		<p>
			${order.address.street}, ${order.address.province},
			${order.address.country}<br> ZIP: ${order.address.zip}<br>
			Phone: ${order.address.phone}
		</p>
		<!-- Items Table -->
		<h3>Items</h3>
		<table border="1" class="table">
			<tr>
				<th>Item Name</th>
				<th>Description</th>
				<th>Price</th>
				<th>Quantity</th>
				<th>Total</th>
			</tr>
			<!-- Iterate Through Order Items -->
			<c:forEach var="item" items="${order.items.items}">
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
	
	<!-- Back Button Using Browser History -->
    <div class="container" style="margin-top: 20px;">
        <button type="button" class="button" onclick="window.history.back();">Go Back</button>
    </div>
</body>
</html>