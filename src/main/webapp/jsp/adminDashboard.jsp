<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
            max-width: 600px;
        }
        .hidden {
            display: none;
        }
        .login-form input {
            display: block;
            margin: 10px auto;
            padding: 10px;
            width: 80%;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .login-form button {
            padding: 10px 20px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .login-form button:hover {
            background-color: #003d80;
        }
        .links .button {
            display: inline-block;
            margin: 10px;
            padding: 10px 20px;
            background-color: #0056b3;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .links .button:hover {
            background-color: #003d80;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Login Form -->
        <div id="loginForm" class="hidden">
            <h1>Admin Login</h1>
            <div class="login-form">
                <input type="text" id="username" placeholder="Username" required />
                <input type="password" id="password" placeholder="Password" required />
                <button onclick="login()">Login</button>
                <p id="errorMessage" style="color: red; display: none;">Invalid username or password!</p>
            </div>
        </div>

        <!-- Dashboard Options -->
        <div id="dashboard" class="hidden">
            <h1>Admin Dashboard</h1>
            <div class="links">
                <a href="AdminController?action=viewSalesHistory" class="button">View Sales</a>
                <a href="AdminController?action=viewInventory" class="button">View Inventory</a>
                <a href="AdminController?action=manageUsers" class="button">Manage Users</a>
                <p>Select an option to view the details.</p>
                <button onclick="logout()">Logout</button>
            </div>
        </div>
    </div>

    <script>
        // Check login state on page load
        document.addEventListener('DOMContentLoaded', () => {
            if (localStorage.getItem('isLoggedIn') === 'true') {
                showDashboard();
            } else {
                showLoginForm();
            }
        });

        function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            if (username === 'admin' && password === 'admin') {
                // Save login state to localStorage
                localStorage.setItem('isLoggedIn', 'true');

                // Show dashboard
                showDashboard();
            } else {
                // Show error message
                document.getElementById('errorMessage').style.display = 'block';
            }
        }

        function logout() {
            // Clear login state
            localStorage.removeItem('isLoggedIn');

            // Show login form
            showLoginForm();
        }

        function showLoginForm() {
            document.getElementById('loginForm').classList.remove('hidden');
            document.getElementById('dashboard').classList.add('hidden');
        }

        function showDashboard() {
            document.getElementById('loginForm').classList.add('hidden');
            document.getElementById('dashboard').classList.remove('hidden');
        }
    </script>
</body>
</html>
