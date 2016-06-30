<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

		</div>
	</div>
	<script type="text/javascript" src="js/moment-with-locales.js"></script>
	<script	src="//cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.8.0/fullcalendar.min.js"></script>
	<script>
	$(document).ready(function() {
	    // page is now ready, initialize the calendar...

	    $('#calendar').fullCalendar({
	        // put your options and callbacks here
	    });
	    
	    $('#prevButton').click(function(){
	    	$('#calendar').fullCalendar('prev');
	    });
	    $('#nextButton').click(function(){
	    	$('#calendar').fullCalendar('next');
	    });
	});
	</script>
</body>
</html>