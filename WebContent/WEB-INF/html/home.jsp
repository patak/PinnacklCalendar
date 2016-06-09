<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.User" />
<jsp:directive.page import="java.util.List" />

<html>
<jsp:directive.include file="header.jsp"/>

<body>
<jsp:directive.include file="navBar.jsp"/>
<div class="container">
	<div class="alert alert-success" role="alert">
		<h1>Hello ${login}</h1>
	</div>
</div>
</body>
</html>