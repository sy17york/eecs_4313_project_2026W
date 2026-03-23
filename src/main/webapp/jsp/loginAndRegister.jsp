<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login or Register</title>
<link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
	<h1>Welcome</h1>

    <!-- Display Error Message -->
    <c:if test="${not empty error}">
        
        <div style="color: red; font-weight: bold;">${error}</div>
    </c:if>

    <!-- Login Form -->
    <h2>Login</h2>
    <form action="AuthenticationController" method="post" class="form">
        <input type="hidden" name="action" value="login">

        <label for="username">Username</label>
        <input type="text" name="username" required>

        <label for="password">Password</label>
        <input type="password" name="password" required>

        <button type="submit" class="button">Login</button>
    </form>

    <!-- Registration Form -->
    <h2>New User? Register Below</h2>
    <form action="AuthenticationController" method="post" class="form">
        <input type="hidden" name="action" value="register">

        <!-- Personal Information -->
        <label for="username">Username</label>
        <input type="text" name="username" required>

        <label for="email">Email</label>
        <input type="email" name="email" required>

        <label for="password">Password</label>
        <input type="password" name="password" required>

        <!-- Address Information -->
        <h3>Address Information</h3>
        <label for="street">Street</label>
        <input type="text" name="street" required>

        <label for="province">Province</label>
        <input type="text" name="province" required>

        <label for="country">Country</label>
        <input type="text" name="country" required>

        <label for="zip">ZIP Code</label>
        <input type="text" name="zip" required>

        <label for="phone">Phone</label>
        <input type="text" name="phone" required>

        <!-- Payment Information -->
        <h3>Payment Info</h3>
        <label for="card_number">Card Number</label>
        <input type="text" name="card_number" required>

        <label for="pin">Pin</label>
        <input type="text" name="pin" required>

        <button type="submit" class="button">Register</button>
    </form>

    <!-- Display Success Message -->
    <c:if test="${not empty message}">
        
        <div style="color: green; font-weight: bold;">${message}</div>
    </c:if>
</body>
<a href="<%= request.getContextPath() %>/ItemController" class="button">back to home</a>
</html>
