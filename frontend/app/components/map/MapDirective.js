/**
 * Die map Direktive ist das Herzstück der Kartenschnittstelle und wird über ein map-HTML Element eingebunden.
 * Sie erzeugt alle notwendigen HTML-Elemente zum Anzeigen der Karte.
 *
 * @author Denis Beck
 */
angular.module('happyHour.map.MapDirective', ['happyHour.map.MapLoader'])
  .directive('map', ['MapLoaderService', function (MapLoaderService) {
    return {
      restrict: 'E',
      scope: {
        /**
         * Allgemeine Optionen für die Karte
         * @type google.Maps.MapOptions
         */
        options: '=',
        /**
         * Die auf der Karte zu markierenden Bars
         * @type Bar[]
         */
        markers: '=',
        /**
         * Die auf der Karte zu zeichnende Route
         * @type Route
         */
        route: '='
      },
      //template: '<div class="map-canvas" style="height:400px; width:80%;"></div>',
      template: '<div class="map-canvas"></div>',
      link: function ($scope, element) {
        MapLoaderService.then(function (maps) {
          var map;

          /**
           * mapOptions setzen
           */
          $scope.$watch('options', function (options) {
            var centerLocation;
            var zoomLevel;
            var alterable;

            // Defaults setzen mit Fokus auf Stuttgart
            centerLocation = new maps.LatLng(48.775556, 9.182778);
            zoomLevel = 14;
            alterable = true;

            // Optionen aus Attribut auslesen
            if (options != null) {
              if (options.centerLocation) {
                centerLocation = new maps.LatLng(
                  options.centerLocation.latitude,
                  options.centerLocation.longitude
                );
              }
              if (options.zoomLevel) {
                zoomLevel = options.zoomLevel;
              }
              if (options.alterable != null) {
                alterable = options.alterable;
              }
            }

            // mapOptions setzen
            var mapOptions = {};
            mapOptions.mapTypeId = maps.MapTypeId.ROADMAP;
            mapOptions.mapTypeControl = false;
            mapOptions.streetViewControl = false;

            //centerLocation setzen
            mapOptions.center = centerLocation;

            // zoomLevel setzen
            mapOptions.zoom = zoomLevel;

            // Veränderungen an der Karte zulassen/blockieren
            //console.log('alterable: ' + alterable);
            mapOptions.draggable = alterable;
            mapOptions.panControl = alterable;
            mapOptions.rotateControl = alterable;
            mapOptions.scaleControl = alterable;
            mapOptions.scrollwheel = alterable;
            mapOptions.zoomControl = alterable;

            map = new maps.Map(element.children()[0], mapOptions);
            //console.log(map);
          });

          /**
           * Markierungen setzen
           */
          $scope.$watch('markers', function (markers) {
            var location;

            // for-Schleife um durch jede Location in markers zu iterieren
            _.forEach(markers, function (marker) {
              if (marker != null) {
                location = new maps.LatLng(marker.location.latitude, marker.location.longitude);
              }

              new maps.Marker({
                map: map,
                position: location
              });
            });
          }, true);

          /**
           * Route setzen
           */
          $scope.$watch('route', function (route) {
            if (route != null && route.timeframes.length >= 2) {
              //console.log(route.timeframes);
              var directionsDisplay;
              var directionsService = new maps.DirectionsService();


              // initialisieren
              directionsDisplay = new maps.DirectionsRenderer();
              directionsDisplay.setMap(map);

              // Wegpunkte definieren
              var startLocation = new maps.LatLng(
                route.timeframes[0].bar.location.latitude,
                route.timeframes[0].bar.location.longitude
              );
              var endLocation = new maps.LatLng(
                route.timeframes[route.timeframes.length - 1].bar.location.latitude,
                route.timeframes[route.timeframes.length - 1].bar.location.longitude
              );

              //console.log("Routenlänge: " + route.timeframes.length + " Ziele");
              var waypoints = [];
              if (route.timeframes.length > 2) {
                //console.log("Route größer als 2 Bars, speichere Zwischenziele");
                for (var i = 1; i < route.timeframes.length - 1; i++) {
                  //console.log(i);
                  var waypointLocation = new maps.LatLng(
                    route.timeframes[i].bar.location.latitude,
                    route.timeframes[i].bar.location.longitude
                  );
                  waypoints.push({
                    location: waypointLocation,
                    stopover: true
                  });
                }
              }

              var request = {
                origin: startLocation,
                destination: endLocation,
                waypoints: waypoints,
                optimizeWaypoints: false,
                travelMode: maps.DirectionsTravelMode.WALKING
              };

              directionsService.route(request, function (response, status) {
                if (status === maps.DirectionsStatus.OK) {

                  /*
                   * DO TRICKY STUFF
                   * Da es keine Möglichkeit gibt InfoWindows der einzelnen Wegpunkte zu ändern,
                   * müssen die Adressen der Wegpunkte geändert werden, da diese von der Map angezeigt werden.
                   */
                  _.forEach(response.routes, function (r) {
                    for (var i = 0; i < r.legs.length; i++) {
                      r.legs[i].start_address =
                        '<b>' + route.timeframes[i].bar.name + '</b><br>' +
                        route.timeframes[i].bar.address + '<br>' +
                        route.timeframes[i].startTime + ' - ' +
                        route.timeframes[i].endTime;
                      r.legs[i].end_address =
                        '<b>' + route.timeframes[i + 1].bar.name + '</b><br>' +
                        route.timeframes[i + 1].bar.address + '<br>' +
                        route.timeframes[i + 1].startTime + ' - ' +
                        route.timeframes[i + 1].endTime;
                    }

                    // Routenwarnungen loggen
                    _.forEach(r.warnings, function (warning) {
                      console.warn(warning);
                    });
                  });
                  directionsDisplay.setDirections(response);
                }
              });
            }
          }, true);
        });
      }
    };
  }]);