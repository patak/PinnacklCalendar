<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.Event" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="../header.jsp"/>

	<h1>${title}</h1>
	<%if (request.getAttribute("success") != null){%>
		<div class="alert alert-success col-md-10" role="alert">
			${success}
		</div>
	<% }else { %>
		<%if (request.getAttribute("errorMessage") != null) { %>
		<div class="alert alert-danger col-md-10" role="alert">
			${errorMessage}
		</div>
		<% } %>
		<form class="form-horizontal col-md-12" action="${action}" method="post" enctype="multipart/form-data">
		  <div class="form-group">
		    <label for="name" class="col-md-2 control-label">Name</label>
		    <div class="col-md-8">
		      <input type="text" class="form-control" id="name" placeholder="Name" name="name">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="description" class="col-md-2 control-label">Description</label>
		    <div class="col-md-8">
		      <textarea class="form-control" rows="3" id="description" name="description"></textarea>
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="place" class="col-md-2 control-label">Place</label>
		    <div class="col-md-8">
		      <input type="text" class="form-control" id="place" placeholder="Place" name="place">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="maps" class="col-md-2 control-label">Find on Google Maps</label>
		    <div class="col-md-8">
		      <div class="input-group">	
		      	<input type="text" class="form-control" id="maps" name="maps">
		      	<span class="input-group-btn">
		      		<button class="btn btn-default" type="button" onclick="findPlace();"/>Localize</button>
		      	</span>
		      </div>
		    </div>
		  </div>
		  
		  <div class="form-group">
			  <div class="col-md-8 col-md-offset-2">
			  	<span id="text_latlng"></span>
			  	<div id="map-canvas" style="height:300px;width:100%"></div>
			  </div>
			  <input type="hidden" name="latitude" id="latitude" value="">
			  <input type="hidden" name="longitude" id="longitude" value="">
		  </div>
		  
		  <div class="form-group">
	  		<label for="startDate" class="col-md-2 control-label">Start Date</label>
		    <div class="col-md-8">
              <div class='input-group date' id='datetimepickerStart'>
                  <input type='text' class="form-control" id="startDate" name="startDate"/>
                  <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span>
                  </span>
              </div>
            </div>
          </div>
          
          <div class="form-group">
	  		<label for="finishDate" class="col-md-2 control-label">Finish Date</label>
		    <div class="col-md-8">
              <div class='input-group date' id='datetimepickerFinish'>
                  <input type='text' class="form-control" id="finishDate" name="finishDate"/>
                  <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span>
                  </span>
              </div>
            </div>
          </div>
          
          <div class="form-group">
		    <label for="photo" class="col-md-2 control-label">Photo</label>
		    <div class="col-md-8">
		      <input type="file" class="form-control" id="photo" name="photo">
		    </div>
		  </div>

		  <div class="form-group">
		    <div class="col-md-10">
		      <input type="submit" class="btn btn-primary btn-lg pull-right" value="${action}" name="submit">
		    </div>
		  </div>
		</form>
	<% } %>

<jsp:directive.include file="../footer.jsp"/>