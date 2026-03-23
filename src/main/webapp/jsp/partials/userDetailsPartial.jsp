<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Details</title>
</head>
<body>
	<h2>User Details</h2>
	<p>Username: ${user.username}</p>
	<p>Email: ${user.email}</p>

<!-- Section: Update Address -->
	<h3>Address</h3>
	<form action="AdminController" method="post">
		<input type="hidden" name="action" value="updateAddress"> <input
			type="hidden" name="userId" value="${user.id}"> <label>Street:</label>
		<input type="text" name="street" value="${address.street}" required>
		<label>Province:</label> <input type="text" name="province"
			value="${address.province}" required> <label>Country:</label>
		<input type="text" name="country" value="${address.country}" required>
		<label>Zip:</label> <input type="text" name="zip"
			value="${address.zip}" required> <label>Phone:</label> <input
			type="text" name="phone" value="${address.phone}" required>
		<button type="submit">Update Address</button>
	</form>

<!-- Section: Update Payment -->
	<h3>Payment</h3>
	<form action="AdminController" method="post">
		<input type="hidden" name="action" value="updatePayment"> <input
			type="hidden" name="userId" value="${user.id}"> <label>Card
			Number:</label> <input type="text" name="cardNumber"
			value="${payment.card_number}" required> <label>Pin:</label>
		<input type="password" name="pin" value="${payment.pin}" required>
		<button type="submit">Update Payment</button>
	</form>

<!-- Section: Purchase History -->
	<h3>Purchase History</h3>
	<c:if test="${not empty orders}">
		<table border="1" class="table">
			<tr>
				<th>Order ID</th>
				<th>Date</th>
				<th>Total Amount</th>
			</tr>
			<c:forEach var="order" items="${orders}">
				<tr>
					<td>${order.orderId}</td>
					<td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd" /></td>
					<td>${order.totalAmount}</td>
					<td><a
						href="AdminController?action=viewOrderDetails&orderId=${order.orderId}">View
							Details</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty orders}">
		<div style="color: red; font-weight: bold;"><p>No purchase history available.</p></div>
	</c:if>
<!-- Back Button -->
        <div style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/AdminController?action=manageUsers" class="button">Go Back</a>
        </div>
</body>
</html>