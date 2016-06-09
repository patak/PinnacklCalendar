<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.User" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:directive.include file="header.jsp"/>

	<table class="table table-striped">
		<thead>
			<tr>
				<th>${title}</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${userList }"> 
				<tr>
					<td><c:out value="${user.pseudo }" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
<jsp:directive.include file="footer.jsp"/>