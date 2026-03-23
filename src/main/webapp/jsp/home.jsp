<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>

<!-- Main Content Section -->
<main>
<!-- Side bar for Sorting and Filtering -->
	<aside>
	 <!-- Sorting Section -->
		<h3>Sort Items</h3>
		<form action="ItemController" method="get" >
			<input type="hidden" name="action" value="sort"> <select
				name="sortBy" onchange="this.form.submit()">
				<option value="">Select Sort Option</option>
				<option value="category">By Category</option>
				<option value="brand">By Brand</option>
				<option value="name">By Name</option>
				<option value="price">By Price(Low - High)</option>
				<option value="priceDesc">By Price(High - Low)</option>
			</select>
		</form>
		<!-- Filter by Category -->
		<h3>Filter by Category</h3>
		<button type="button" onclick="toggleCategoryList()">Categories</button>
		<ul id="categoryList" style="display: none;">
			<c:forEach var="category" items="${categories}">
				<li><a
					href="ItemController?action=filtercate&categoryId=${category.id}">
						${category.categoryDescription} </a></li>
			</c:forEach>
		</ul>
		<!-- Filter by Brand -->
		<h3>Filter by Brand</h3>
		<button type="button" onclick="toggleBrandList()">Brands</button>
		<ul id="brandList" style="display: none;">
			<c:forEach var="brand" items="${brands}">
				<li><a
					href="ItemController?action=filterbrand&brand=${brand}">
						${brand} </a></li>
			</c:forEach>
		</ul>
		
	</aside>

	<!-- Section for Displaying Items -->
	<section>
		<h3>Item Results</h3><a href="ItemController">Show All Items</a>
		<c:if test="${not empty items && empty itemDetails}">
			<table border="1" class="table">
				<tr>
					<th>Image</th>
					<th>Category</th>
					<th>Brand</th>
					<th>Item Name</th>
					
					<th>Price</th>
					
					<th>Action</th>
				</tr>
				 <!-- Iterate Through Items -->
				<c:forEach var="item" items="${items}">
					<tr>
						<td><a href="ItemController?action=viewItemDetails&itemId=${item.itemId}">
							    <img
							        src="<c:out value='${pageContext.request.contextPath}/images/${item.itemId}.jpg'/>"
							        alt="Image"
							        width="100"
							        height="100"
							        onerror="this.src='${pageContext.request.contextPath}/images/${item.category.categoryDescription.toLowerCase()}.jpg';">
							</a>
						</td>
						<td>${item.category.categoryDescription}</td>
					
						<td>${item.brand}</td>
						<td><a
						href="ItemController?action=viewItemDetails&itemId=${item.itemId}">${item.name}</a></td>
						
						<td>${item.price}</td>
						
						<td>
							<form action="ShoppingCartController" method="post">
								<input type="hidden" name="action" value="add"> <input
									type="hidden" name="itemId" value="${item.itemId}"> <input
									type="number" name="quantity" value="1" min="1" />
								<button type="submit">Add to Cart</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	
			
			<!-- order detail section -->
		<c:if test="${not empty itemDetails}">
		
		    <h2>Item Details</h2>
		    
			
			<table border="1">
				<tr>
					<th>Image</th>
					<th>Category</th>
					<th>Brand</th>
					<th>Item Name</th>
					<th>Description</th>
					<th>Price</th>
				
					<th>Stock</th>
					<th>Action</th>
				</tr>
				
					<tr>
						<td><img
							src="<c:out value='${pageContext.request.contextPath}/images/${itemDetails.itemId}.jpg'/>"
							alt="Image" width="150" height="150"
							onerror="this.src='${pageContext.request.contextPath}/images/${itemDetails.category.categoryDescription.toLowerCase()}.jpg';">
						</td>
						<td>${itemDetails.category.categoryDescription}</td>
						<td>${itemDetails.brand}</td>
						<td>${itemDetails.name}</td>
						<td>${itemDetails.description}</td>
						<td>${itemDetails.price}</td>
				
						<td>${itemDetails.stockQuantity}</td>
						<td>
							<form action="ShoppingCartController" method="post">
								<input type="hidden" name="action" value="add"> <input
									type="hidden" name="itemId" value="${itemDetails.itemId}"> <input
									type="number" name="quantity" value="1" min="1" />
								<button type="submit">Add to Cart</button>
							</form>
						</td>
					</tr>
				
			</table>
			
			<a href="#" onclick="history.back(); return false;">Go Back</a>
		</c:if>
		
		<c:if test="${empty items && empty itemDetails}">
			<div style="color: red; font-weight: bold;">No items found.</div>
			
		</c:if>
	</section>
</main>


<script>
    // Toggle visibility of the category list
    function toggleCategoryList() {
        const categoryList = document.getElementById('categoryList');
        if (categoryList.style.display === 'none') {
            categoryList.style.display = 'block';
        } else {
            categoryList.style.display = 'none';
        }
    }
    function toggleBrandList() {
        const brandList = document.getElementById('brandList');
        if (brandList.style.display === 'none') {
        	brandList.style.display = 'block';
        } else {
        	brandList.style.display = 'none';
        }
    }
</script>
