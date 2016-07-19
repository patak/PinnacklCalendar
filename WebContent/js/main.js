$(document).ready(function () {
  /**
   * Full calendar
   */
  $('#calendar').fullCalendar({
    header: {
      left: '',
      right: ''
    },
    dayClick: function(date, jsEvent, view) {

        // console.info('---');
      // console.log('Clicked on: ' + date.format());
        // console.log('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
        // console.log('Current view: ' + view.name);

        // FIXME: remove handler until we know how to manage event creation
        // $('.add-button').remove();
        // $('.fc-today').removeClass('fc-today');
        // $('.fc-state-highlight').removeClass('fc-state-highlight');

        // // change the day's background color just for fun
        // $(this).addClass('fc-state-highlight fc-today');
        // $(this).append('<button class="add-button btn btn-default">+</button>');

    },
    handleWindowResize: true,
    height: getCalendarHeight(),
    events: function(start, end, timezone, callback) {
      // console.log(start.unix());
      $.ajax({
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
              var title = typeof evnt.name !== 'undefined' ? evnt.name : null;
              var start = typeof evnt.startDate !== 'undefined' ? evnt.startDate : null;
              var end = typeof evnt.finishDate !== 'undefined' ? evnt.finishDate : null;
              var description = typeof evnt.description !== 'undefined' ? evnt.description : null;
              // ...

              events.push({
                title: title,
                start: start,
                end: end,
                description,
              });
            });
            callback(events);
          }
      });
    },
    eventClick: function(calEvent, jsEvent, view) {
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
            <p>One fine body&hellip;</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Save changes</button>
          </div>
        </div><!-- /.modal-content -->
      `;

      // console.log(template);
      $(".modal-dialog").html(template);
      $('#showEventModal').modal();
    },
  });

  $(window).resize(function() {
    $('#calendar').fullCalendar('option', 'height', getCalendarHeight());
  });

  currDayCol = "";
  var moment = $('#calendar').fullCalendar('getDate');
  if (moment.format) {
    var currDayCol = moment.format('ddd');
    $('.dataToday').html(moment.format('MMMM YYYY'));
  }

  // Add calendar button listeners
  $('th.fc-day-header.fc-widget-header.fc-' + currDayCol.toLowerCase()).addClass('current-day-column');
  
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
