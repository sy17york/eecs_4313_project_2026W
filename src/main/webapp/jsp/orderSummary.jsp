<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Order Summary</title>
</head>
<body>
	<h1>Order Summary</h1>
<!-- Check if Order Review is Available -->
	<c:if test="${not empty order_review}">
		<div>
			<h3>Order Confirmed</h3>
			<p>
				<strong>Order ID:</strong> ${order_review.orderId}
			</p>
			<p>
				<strong>Customer:</strong> ${order_review.customer.username}
				(${order_review.customer.email})
			</p>
			<p>
				<strong>Date:</strong> <fmt:formatDate value="${order_review.orderDate}" pattern="yyyy-MM-dd HH:mm:ss" />
			</p>
			<p>
				<strong>Total Amount:</strong> ${order_review.totalAmount}
			</p>
			<p>
				<strong>Paid By:</strong> ${card}
			</p>
			<p>
				<strong>Status:</strong> ${order_review.status}
			</p>
	
			<h2>Shipping Address</h2>
			<p>
				${order_review.address.street}, ${order_review.address.province},
				${order_review.address.country}<br> ZIP: ${order_review.address.zip}<br>
				Phone: ${order_review.address.phone}
			</p>
	</div>
			<h3>Items</h3>
				<table border="1">
					<tr>
						<th>Item Name</th>
						<th>Description</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Total</th>
					</tr>
					<c:forEach var="item" items="${order_review.items.items}">
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
	<a href="<%= request.getContextPath() %>/ItemController" class="button">back to home</a>
</body>
</html>