<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.User" />
<jsp:directive.page import="java.util.List" />


<jsp:directive.include file="header.jsp"/>

	<div class="alert alert-success" role="alert">
		<h1>Hello ${login}</h1>
	</div>

<jsp:directive.include file="footer.jsp"/>