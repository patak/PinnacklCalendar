<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.User" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="../header.jsp"/>

	<h1>${title}</h1>

	<%if (request.getAttribute("success") != null){%>
		<div class="alert alert-success" role="alert">
			${success}
		</div>
	<% }else { %>
		<%if (request.getAttribute("errorMessage") != null) { %>
			<div class="alert alert-danger" role="alert">
				${errorMessage}
			</div>
		<% } %>
		<form class="form-horizontal eventForm" action="${action}" method="post">
			<% if (request.getAttribute("action") != "change") { %>
				<% if (request.getAttribute("action") == "create") { %>
					<div class="form-group">
					  <label for="email" class="col-sm-2 control-label">Email</label>
					  <div class="col-sm-8">
					    <input type="email" class="form-control" id="email" placeholder="Email" name="email" required>
					  </div>
					</div>
				<% } %>
				<div class="form-group">
				  <label for="pseudo" class="col-sm-2 control-label">Pseudo</label>
				  <div class="col-sm-8">
				    <input type="text" class="form-control" id="pseudo" placeholder="Pseudo" name="pseudo" required>
				  </div>
				</div>
				<div class="form-group">
				  <label for="password" class="col-sm-2 control-label">Password</label>
				  <div class="col-sm-8">
				    <input type="password" class="form-control" id="password" placeholder="Password" name="password" required>
				  </div>
				</div>
		  <% } else { %>
	  		<div class="form-group">
				<label for="currentPassword" class="col-sm-2 control-label">Current Password</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" id="currentPassword" placeholder="Current Password" name="currentPassword">
				</div>
			</div>
		  	<div class="form-group">
				<label for="newPassword" class="col-sm-2 control-label">New Password</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" id="newPassword" placeholder="New Password" name="newPassword">
				</div>
			</div>
			<div class="form-group">
				<label for="confirmNewPassword" class="col-sm-2 control-label">Confirm New Password</label>
				<div class="col-sm-8">
					<input type="password" class="form-control" id="confirmNewPassword" placeholder="Confirm New Password" name="confirmNewPassword">
				</div>
			</div>
		  <% } %>
		  <div class="form-group">
		    <div class="col-sm-10">
		      <button type="submit" class="btn btn-primary btn-lg pull-right">${action}</button>
		    </div>
		  </div>
		</form>
	<% } %>

<jsp:directive.include file="../footer.jsp"/>
