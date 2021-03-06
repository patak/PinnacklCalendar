$(document).ready(function () {
  /**
   * Full calendar
   */
  $('#calendar').fullCalendar({
    header: {
      left: '',
      right: ''
    },
    nowIndicator: true,
    editable: true,
    minTime: "00:00:00",
    maxTime: "23:59:59",
    allDayDefault: false,
    handleWindowResize: true,
    height: getCalendarHeight(),
    events: function(start, end, timezone, callback) {
      // console.log(start.unix());
      $.ajax({
          // FIXME : Get the current user ID from the rest API
          url: 'api/events',
          dataType: 'json',
          data: {
              // our hypothetical feed requires UNIX timestamps
              start: start.unix(),
              end: end.unix()
          },
          success: function(doc) {
            var events = [];
            $(doc).each(function(idx, evnt) {
              var id = typeof evnt.id !== 'undefined' ? evnt.id : null;
              var title = typeof evnt.name !== 'undefined' ? evnt.name : null;
              var start = typeof evnt.startDate !== 'undefined' ? evnt.startDate : null;
              var end = typeof evnt.finishDate !== 'undefined' ? evnt.finishDate : null;
              var description = typeof evnt.description !== 'undefined' ? evnt.description : null;
              var sharedEvent = typeof evnt.sharedEvent !== 'undefined' ? evnt.sharedEvent : null;
              var sharedUsers = typeof evnt.sharedUsers !== 'undefined' ? evnt.sharedUsers : null;
              var place = typeof evnt.place !== 'undefined' ? evnt.place : null;
              var latitude = typeof evnt.latitude !== 'undefined' ? evnt.latitude : null;
              var longitude = typeof evnt.longitude !== 'undefined' ? evnt.longitude : null;
              // ...

              var eventObj = {
                id: id,
                title: title,
                start: start,
                end: end,
                description: description,
                place: place,
                latitude: latitude,
                longitude: longitude,
              };

              if (sharedEvent) {
                eventObj.color = "#F44336";
                eventObj.sharedUsers = sharedUsers;
              }

              events.push(eventObj);
            });
            callback(events);
          }
      });
    },
    eventClick: function(calEvent, jsEvent, view) {

      var friendsTpl = "";
      if (calEvent.sharedUsers) {
        for (var i = calEvent.sharedUsers.length - 1; i >= 0; i--) {
          friendsTpl += `
            <span class="user-circle tooltip">
              <span class="tooltiptext">${calEvent.sharedUsers[i].pseudo}</span>
              <span>${calEvent.sharedUsers[i].pseudo.charAt(0).toUpperCase()}</span>
            </span>
          `;
        };
      }

      friendsTpl += `
        <span class="user-circle add tooltip">
          <span class="tooltiptext">Add</span>
          <a href="/PinnacklCalendar/edit?id=${calEvent.id}">+</a>
        </span>
      `;

      // Create a string template to generate the modal content
      var template = `
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title">${calEvent.title}</h4>
          </div>
          <div class="modal-body">
            <p class="modal-section">Date</p>
            <p>${computeDate(calEvent.start, calEvent.end)}</p>
            <p class="modal-section">Description</p>
            <p>${calEvent.description}</p>
            <p class="modal-section">Place</p>
            <p>${calEvent.place}</p>
            <div id="map"></div>
            <p class="modal-section">Participant</p>
            <p>${friendsTpl}</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-secondary"><a href="/PinnacklCalendar/edit?id=${calEvent.id}">Edit event</a></button>
          </div>
        </div><!-- /.modal-content -->
        <input type="hidden" id="latitude">
        <input type="hidden" id="longitude">
      `;

      // console.log(template);
      $(".modal-dialog").html(template);
      $('#showEventModal').modal();
    },
    viewRender: function () {
      var currentDay = window.moment().format('ddd').toLowerCase();
      var moment = $('#calendar').fullCalendar('getDate');
      var currDayCol = moment.format('ddd');

      if (currentDay != currDayCol) {
        currDayCol = currentDay;
      }

      // Add calendar button listeners
      $('th.fc-day-header.fc-widget-header.fc-' + currDayCol.toLowerCase()).addClass('current-day-column');

      $('.dataToday').html(moment.format('MMMM YYYY'));
    }
  });

  $(window).resize(function() {
    $('#calendar').fullCalendar('option', 'height', getCalendarHeight());
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
    $('#calendar').fullCalendar( 'changeView', 'agendaWeek' );
  });
  $('#dayButton').click(function(){
    $('#calendar').fullCalendar( 'changeView', 'agendaDay' );
  });

  /**
   * Compute the height of the calendar
   * @return {[type]} [description]
   */
  function getCalendarHeight() {
    return ($(window).height()*75)/100;
  }

  /**
   * Compute the date string Date start - Date end
   * @param  {Moment} dateStartObject  [description]
   * @param  {Moment} dateFinishObject [description]
   * @return {String}                  [description]
   */
  function computeDate (dateStartObject, dateFinishObject) {
    var dateEnd = "",
        separator = "";
    if (dateFinishObject) {
      dateEnd = dateFinishObject.format('ddd, D MMM YYYY HH:mm');
      separator = "-";
    }

    if (dateStartObject) {
      return `${dateStartObject.format('ddd, D MMM YYYY HH:mm')} ${separator} ${dateEnd}`;
    }

    return dateEnd;
  }

  /**
   * Share multiple email
   */
  $("#share").multiple_emails({
     position: 'top', // Display the added emails above the input
     theme: 'bootstrap', // Bootstrap is the default theme
     checkDupEmail: true // Should check for duplicate emails added
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

    $('#finishDate').bootstrapMaterialDatePicker({weekStart: 0, format : 'DD/MM/YYYY HH:mm'});
    $('#startDate').bootstrapMaterialDatePicker({weekStart: 0, format : 'DD/MM/YYYY HH:mm'}).on('change', function(e, date) {
      $('#finishDate').bootstrapMaterialDatePicker('setMinDate', date);
    });

    $('#datetimepickerStart button').click(function (e) {
      console.log('variable');
      $('#startDate').trigger("focus");
    });
    $('#datetimepickerFinish button').click(function (e) {
      $('#finishDate').trigger("focus");
    });
  }
  // initialisation de la carte Google Map de départ
  function initMap() {
    geocoder = new google.maps.Geocoder();
    // Ici j'ai mis la latitude et longitude
    if(document.getElementById('latitude').value && document.getElementById('longitude').value) {
    	var latlng = new google.maps.LatLng(document.getElementById('latitude').value, document.getElementById('longitude').value);
    } else {
    	var latlng = new google.maps.LatLng(48.856614, 2.3522219000000177);
    }
    var mapOptions = {
      zoom      : 14,
      center    : latlng,
      mapTypeId : google.maps.MapTypeId.ROADMAP
    }
    // map-canvas est le conteneur HTML de la carte Google Map
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    
    if(document.getElementById('latitude').value && document.getElementById('longitude').value) {
    	// Création du marqueur du lieu (épingle)
        marker = new google.maps.Marker({
            map: map,
            position: latlng
        });
    }
  }

  function findPlace() {
    // Réinitialisation du marqueur
    if(marker != null)
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

  /**
   * Add Google Map find place listener
   */
  $('.findPlaceBtn').click(function (e) {
    findPlace();
  });
});
