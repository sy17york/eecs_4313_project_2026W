<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Welcome</title>
<style>
body {
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	margin: 0;
	background-color: #f9f9f9;
}

.container {
	text-align: center;
	padding: 20px;
	background: white;
	border-radius: 10px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	width: 80%;
	max-width: 400px;
}

.button {
	display: block;
	margin: 15px auto;
	padding: 10px 20px;
	background-color: #0056b3;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s;
	text-align: center;
	width: 200px;
}

.button:hover {
	background-color: #003d80;
}
</style>
</head>
<body>
	<div class="container">
		<h1>Welcome</h1>
		<p>Select an option to proceed:</p>
		<a href="<%= request.getContextPath() %>/admin" class="button">Admin
			Dashboard</a> <a href="<%= request.getContextPath() %>/ItemController"
			class="button">Home Page</a>
	</div>
</body>
</html>
