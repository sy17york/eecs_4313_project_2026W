<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Shopping Cart</title>
<link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
	<h1>Shopping Cart</h1>

	<!-- Display error message if available -->
	<c:if test="${not empty error}">
		
		<div style="color: red; font-weight: bold;">${error}</div>
	</c:if>

	<!-- Check if Cart is Not Empty -->
	<c:if test="${cart != null && !cart.items.isEmpty()}">
	<!-- Cart Items Table -->
		<table border="1" class="table" cellpadding="6">
			<tr>
				<th>Item</th>
				<th>Description</th>
				<th>Price</th>
				<th>Quantity</th>
				<th>Total</th>
				<th>Action</th>
			</tr>
			 <!-- Iterate Through Cart Items -->
			<c:forEach var="cartItem" items="${cart.items}">
				<tr>
					<td>${cartItem.item.name}</td>
					<td>${cartItem.item.description}</td>
					<td>${cartItem.item.price}</td>
					<td>${cartItem.quantity}</td>
					<td>${cartItem.priceAtPurchase}</td>
					<td>
					 <!-- Update and Remove Actions -->
						<form action="ShoppingCartController" method="post">
							<input type="hidden" name="action" value="update"> <input
								type="hidden" name="itemId" value="${cartItem.item.itemId}">
							<input type="number" name="quantity" value="${cartItem.quantity}"
								min="1">
							<button type="submit">Update</button>
						</form>
						<form action="ShoppingCartController" method="post">
							<input type="hidden" name="action" value="remove"> <input
								type="hidden" name="itemId" value="${cartItem.item.itemId}">
							<button type="submit">Remove</button>
						</form>
					</td>
				</tr>
			</c:forEach>
			<tr>
			<!-- Total Price Row -->
				 <td colspan="4"><strong>Total Price</strong></td>
				<td>${cart.getTotalPrice()}</td>
				<td></td>
			</tr>
		</table>
		<!-- Confirm Order Button -->
		<form action="OrderController" method="get">
			<input type="hidden" name="action" value="confirmOrder">
			<button type="submit">Check Out</button>
		</form>
	</c:if>
	<!-- Empty cart message -->
	<c:if test="${cart == null || cart.items.isEmpty()}">
		<p>Your shopping cart is empty.</p>		
	</c:if>
	<button onclick="window.location.href='ItemController';">Continue
			Shopping</button>
</body>
</html>
