$(document).ready(function () {
	/**
	 * Full calendar
	 */
    $('#calendar').fullCalendar({
        // put your options and callbacks here
    });
    
    $('#prevButton').click(function(){
    	$('#calendar').fullCalendar('prev');
    });
    $('#nextButton').click(function(){
    	$('#calendar').fullCalendar('next');
    });
    $('#todayButton').click(function(){
    	$('#calendar').fullCalendar('today');
    });
    $('#monthButton').click(function(){
    	$('#calendar').fullCalendar( 'changeView', 'month' );
    });
    $('#weekButton').click(function(){
    	$('#calendar').fullCalendar( 'changeView', 'basicWeek' );
    });
    $('#dayButton').click(function(){
    	$('#calendar').fullCalendar( 'changeView', 'basicDay' );
    });

    /**
     * Add Google Map
     */
    var geocoder;
	var map;
	var marker;

	var map = document.getElementById('map-canvas');

	// Lunch google map if the canvas exit
	// so if we are on the right page
	if (map) {
		// Lancement de la construction de la carte google map
		google.maps.event.addDomListener(window, 'load', initMap);

		$('#datetimepickerStart').datetimepicker({
		    locale: 'fr'
		});
		$('#datetimepickerFinish').datetimepicker({
		    locale: 'fr'
		});
	}
});

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