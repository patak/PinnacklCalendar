<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.Event" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="header.jsp"/>
	<button id="prevButton" class="btn btn-default">Previous</button>
	<button id="nextButton" class="btn btn-default">Next</button>
	<div id="calendar">
	</div>
<jsp:directive.include file="footer.jsp"/>