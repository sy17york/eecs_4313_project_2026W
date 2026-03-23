<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
<!-- Section: Manage Users -->
	<h2>Manage Users</h2>
	<!-- Back to Dashboard Link -->
	<a href="admin">Back to Dash Board</a>
	<br></br><hr><br></br>
	
	<!-- Check if User List is Not Empty -->
	<c:if test="${not empty users}">
		 <table border="1" class="table">
			<tr>
				<th>User ID</th>
				<th>Username</th>
				<th>Email</th>
				<th>Actions</th>
			</tr>
			 
			 <!-- Display User Details -->
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.id}</td>
					<td>${user.username}</td>
					<td>${user.email}</td>
					<td><a
						href="AdminController?action=viewUserDetails&userId=${user.id}">View
							Details</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	 <!-- Message When No Users Are Available -->
	<c:if test="${empty users}">
		<div style="color: red; font-weight: bold;"><p>No users available.</p></div>
	</c:if>
	
</div>
