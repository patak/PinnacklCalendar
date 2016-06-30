<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.Event" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="header.jsp"/>
	<div id="calendar"></div>
<script>
$(document).ready(function() {

    // page is now ready, initialize the calendar...

    $('#calendar').fullCalendar({
        // put your options and callbacks here
    });

});
</script>
<jsp:directive.include file="footer.jsp"/>