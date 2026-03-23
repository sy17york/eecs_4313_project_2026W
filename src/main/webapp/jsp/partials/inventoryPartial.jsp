<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
<!-- Check and Display Success Message -->
	<c:if test="${not empty message}">
		<div style="color: green; font-weight: bold;">${message}</div>
		<c:remove var="message" />
	</c:if>
	
	<!-- Section Title -->
	<h2>Manage Inventory</h2>
	<a href="admin">Back to Dash Board</a>
<br></br><hr><br></br>
	<!-- Add New Item Form -->
	<form action="/eecs_4413_project5/AdminController" method="post" class="form">
    <input type="hidden" name="action" value="insertItem">
    <label>Name:</label>
    <input type="text" name="name" required>
    <label>Description:</label>
    <textarea name="description" required></textarea>
    <label>Category:</label>
    <select name="categoryId" required>
        <option value="">Select Existing Category</option>
        <c:forEach var="category" items="${categories}">
            <option value="${category.id}">${category.categoryDescription}</option>
        </c:forEach>
    </select>
    
    <!-- Input: New Category -->
    <label>New Category:</label>
    <input type="text" name="categoryName" placeholder="Leave blank if using existing category">
    
     <!-- Input: Item Brand -->
    <label>Brand:</label>
    <input type="text" name="brand" required>
     
     <!-- Input: Stock Quantity -->
    <label>Stock Quantity:</label>
    <input type="number" name="stockQuantity" required>
    
    <!-- Input: Price -->
    <label>Price:</label>
    <input type="number" name="price" step="0.01" required>
    
    <!-- Submit Button -->
    <button type="submit">Add Item</button>
</form>

	<!-- Inventory Table -->
	<h3>Existing Inventory</h3>
	<c:if test="${not empty inventory}">
		<table border="1">
			<tr>
				<th>Image</th>
				<th>Item ID</th>
				<th>Category ID</th>
				<th>Brand</th>
				<th>Name</th>
				<th>Stock</th>
				<th>Price</th>
				<th>Actions</th>
			</tr>
			 <!-- Iterate through Inventory Items -->
			<c:forEach var="item" items="${inventory}">
				<tr>
				 <!-- Display Item Image -->
					<td><img
						src="${pageContext.request.contextPath}/images/${item.itemId}.jpg"
						alt="${item.name}" width="50" height="50"
						onerror="this.src='${pageContext.request.contextPath}/images/${item.category.categoryDescription.toLowerCase()}.jpg';">
					</td>
					<!-- Display Item Details -->
					<td>${item.itemId}</td>
					<td>${item.category.categoryDescription}</td>
					<td>${item.brand}</td>
					<td>${item.name}</td>
					<td>${item.stockQuantity}</td>
					<td>${item.price}</td>
					<td>
					<!-- Action Buttons: update Inventory -->
						<form action="AdminController" method="post">
							<input type="hidden" name="action" value="updateInventory">
							<input type="hidden" name="id" value="${item.itemId}"> 
							<input type="number" name="quantity" value="${item.stockQuantity}" required>
							<button type="submit">Update Inventory</button>
						</form>
						<!-- Action Buttons: Upload Image -->
						<form action="AdminController" method="post" enctype="multipart/form-data">
							<input type="hidden" name="action" value="uploadImage"> 
							<input type="hidden" name="itemId" value="${item.itemId}"> 
								<input type="file" name="itemImage" accept=".jpg" required>
							<button type="submit">Upload Image</button>
						</form>
						<!-- Action Buttons: Delete Item -->
						<form action="AdminController" method="post">
							<input type="hidden" name="action" value="removeItem"> 
							<input type="hidden" name="itemId" value="${item.itemId}">
							<button type="submit">Remove Item</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty inventory}">
	<div style="color: red; font-weight: bold;"><p>No inventory available.</p></div>
		
	</c:if>
	
</div>
