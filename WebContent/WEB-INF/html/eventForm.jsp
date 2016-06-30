<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:directive.page import="fr.pinnackl.beans.Event" />
<jsp:directive.page import="java.util.List" />

<jsp:directive.include file="headerEvent.jsp"/>

	<h1>${title}</h1>
	<%if (request.getAttribute("success") != null){%>
		<div class="alert alert-success col-sm-10" role="alert">
			${success}
		</div>
	<% }else { %>
		<%if (request.getAttribute("errorMessage") != null) { %>
		<div class="alert alert-danger col-sm-10" role="alert">
			${errorMessage}
		</div>
		<% } %>
		<form class="form-horizontal col-sm-12" action="${action}" method="post" enctype="multipart/form-data">
		  <div class="form-group">
		    <label for="name" class="col-sm-2 control-label">Name</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="name" placeholder="Name" name="name">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="description" class="col-sm-2 control-label">Description</label>
		    <div class="col-sm-8">
		      <textarea class="form-control" rows="3" id="description" name="description"></textarea>
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="place" class="col-sm-2 control-label">Place</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="place" placeholder="Place" name="place">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label for="maps" class="col-sm-2 control-label">Find on Google Maps</label>
		    <div class="col-sm-8">
		      <div class="input-group">	
		      	<input type="text" class="form-control" id="maps" name="maps">
		      	<span class="input-group-btn">
		      		<button class="btn btn-default" type="button" onclick="findPlace();"/>Localize</button>
		      	</span>
		      </div>
		    </div>
		  </div>
		  
		  <div class="form-group">
			  <div class="col-sm-10">
			  	<span id="text_latlng"></span>
			  	<div id="map-canvas" style="height:300px;width:100%"></div>
			  </div>
			  <input type="hidden" name="latitude" id="latitude" value="">
			  <input type="hidden" name="longitude" id="longitude" value="">
		  </div>
		  
		  <div class="form-group">
	  		<label for="startDate" class="col-sm-2 control-label">Start Date</label>
		    <div class="col-sm-8">
              <div class='input-group date' id='datetimepickerStart'>
                  <input type='text' class="form-control" id="startDate" name="startDate"/>
                  <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span>
                  </span>
              </div>
            </div>
          </div>
          
          <div class="form-group">
	  		<label for="finishDate" class="col-sm-2 control-label">Finish Date</label>
		    <div class="col-sm-8">
              <div class='input-group date' id='datetimepickerFinish'>
                  <input type='text' class="form-control" id="finishDate" name="finishDate"/>
                  <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span>
                  </span>
              </div>
            </div>
          </div>
          
          <div class="form-group">
		    <label for="photo" class="col-sm-2 control-label">Photo</label>
		    <div class="col-sm-8">
		      <input type="file" class="form-control" id="photo" name="photo">
		    </div>
		  </div>

		  <div class="form-group">
		    <div class="col-sm-10">
		      <button type="submit" class="btn btn-primary btn-lg pull-right" name="submit">${action}</button>
		    </div>
		  </div>
		</form>
	<% } %>
	

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC6uNEPCTx8tDMyc6PJX420mGfV4zBC0I4">
</script>


<script type="text/javascript">
var geocoder;
var map;
var marker;

// initialisation de la carte Google Map de départ
function initMap() {
  geocoder = new google.maps.Geocoder();
  // Ici j'ai mis la latitude et longitude du vieux Port de Marseille pour centrer la carte de départ
  var latlng = new google.maps.LatLng(48.856614, 2.3522219000000177);
  var mapOptions = {
    zoom      : 14,
    center    : latlng,
    mapTypeId : google.maps.MapTypeId.ROADMAP
  }
  // map-canvas est le conteneur HTML de la carte Google Map
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}
 
function findPlace() {
  // Réinitialisation du marqueur		
  if(marker !=null)
	  marker.setMap(null);
  // Récupération de l'adresse tapée dans le formulaire
  var adresse = document.getElementById('maps').value;
  geocoder.geocode( { 'address': adresse}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      // Récupération des coordonnées GPS du lieu tapé dans le formulaire
      var strposition = results[0].geometry.location+"";
      strposition=strposition.replace('(', '');
      strposition=strposition.replace(')', '');
      // Affichage des coordonnées dans le <span>
      document.getElementById('text_latlng').innerHTML='Coordonnées : '+strposition;
      //Récupération des coordonnées dans le formulaire
      document.getElementById('latitude').value=results[0].geometry.location.lat();
      document.getElementById('longitude').value=results[0].geometry.location.lng();
      // Création du marqueur du lieu (épingle)
      marker = new google.maps.Marker({
          map: map,
          position: results[0].geometry.location
      });
    } else {
      alert('Adresse introuvable: ' + status);
    }
  });
}
// Lancement de la construction de la carte google map
google.maps.event.addDomListener(window, 'load', initMap);
</script>

<script type="text/javascript">
    $(function () {
        $('#datetimepickerStart').datetimepicker({
            locale: 'fr'
        });
        $('#datetimepickerFinish').datetimepicker({
            locale: 'fr'
        });
    });
</script>

<jsp:directive.include file="footer.jsp"/>