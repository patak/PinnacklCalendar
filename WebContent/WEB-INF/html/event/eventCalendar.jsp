<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.Event" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="../header.jsp"/>
	<div class="calendar">
		<div class="calendar-control left-button">
			<button id="prevButton" class="btn btn-default">
				<span class="glyphicon glyphicon-chevron-left"></span>
				Previous
			</button>
			<button id="nextButton" class="btn btn-default">
				Next
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>
			<button id="todayButton" class="btn btn-default"><i class="material-icons calendar-icon">today</i>Today</button>
			<span class="dataToday"></span>
		</div>
		<div class="calendar-control right-button">
			<button id="monthButton" class="btn btn-default">Month</button>
			<button id="weekButton" class="btn btn-default">Week</button>
			<button id="dayButton" class="btn btn-default">Day</button>
		</div>
	</div>
	<div id="calendar">
	</div>
	<div id="showEventModal" class="modal fade">
	  <div class="modal-dialog" role="document">
	  	<!--
	  		The modal content goes here
	  		The data comes from javascript
	  	-->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
<jsp:directive.include file="../footer.jsp"/>