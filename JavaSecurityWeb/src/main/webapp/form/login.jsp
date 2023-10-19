<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" />
		<title>Login</title>
	</head>
	<body style="padding: 20px">
		
		<form method="post" action="../secure/servlet/form/login" class="pure-form">
			
			Username：<input type="text" id="username" name="username" /> Ex: user<p />
			
			Password：<input type="password" id="password" name="password" /> Ex: 1234<p />
			
			<input type="submit" value="Login" class="pure-button pure-button-primary" /><p />
			
		</form>
		
	</body>
</html>