<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<header>
<!-- Header Section -->

<!-- Logo/Store Title -->
	<h1><a href="<%= request.getContextPath() %>/ItemController" class="button">Online Store</a></h1>
  <!-- Navigation Links -->
	<nav>
		<c:choose>
			
			<c:when test="${not empty sessionScope.user}">
			<!-- Display when user is logged in -->
				<button
					onclick="window.location.href='UserController?action=profile';">View
					Profile</button>
				
				<button
					onclick="window.location.href='AuthenticationController?action=logout';">Logout</button>
			</c:when>
		
			<c:otherwise>
			<!-- Display when user is NOT logged in -->
				<button
					onclick="window.location.href='AuthenticationController?action=loginPage';">Login</button>
			</c:otherwise>
		</c:choose>
		
		<button onclick="window.location.href='ShoppingCartController?action=viewCart';">My Cart</button>
	</nav>
	
	<form action="ItemController" method="get">
		<input type="text" name="keyword" placeholder="Search items..." /> <input
			type="hidden" name="action" value="search">
		<button type="submit">Search</button>
	</form>
	
</header>
