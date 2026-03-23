<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div>
	<h2>Sales History</h2>
<a href="admin" class="button">Back to Dashboard</a>
<br></br><hr><br></br>
	<!-- Filter Section -->
	<form action="AdminController" method="get">
		<input type="hidden" name="action" value="filterSales"> <label>Customer
			:</label> <input type="text" name="username" /> <label>Product
			:</label> <input type="text" name="productName" /> <label>Date: from</label> <input
			type="date" name="date1" /> to <input
			type="date" name="date2" />
		<button type="submit">Filter</button>
	</form>

	<!-- Sales History Table -->
	<c:if test="${not empty sales}">
		<table border="1">
			<tr>
				<th>Order ID</th>
				<th>Customer</th>
				<th>Date</th>
				<th>Total Amount</th>
				
			</tr>
			<c:forEach var="order" items="${sales}">
				<tr>
					<td>${order.orderId}</td>
					<td>${order.customer != null ? order.customer.username : 'Unknown'}</td>
					<td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd" /></td>
					
					<td>${order.totalAmount}</td>
					<td><a
						href="AdminController?action=viewOrderDetails&orderId=${order.orderId}">View
							Details</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty sales}">
		<div style="color: red; font-weight: bold;"><p>No sales records found.</p></div>
	</c:if>

	
</div>
