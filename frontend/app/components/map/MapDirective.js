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
      template: '<div class="map-canvas" style="height:400px; width:80%;"></div>',
      link: function ($scope, element) {
        MapLoaderService.then(function (maps) {
          var mapOptions = {
            //center: new maps.LatLng($scope.options.center.latitude, $scope.options.center.longitude),
            center: new maps.LatLng(48.775556, 9.182778),
            zoom: 14,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            streetViewControl: false,
            mapTypeControl: false
          };
          var map = new maps.Map(element.children()[0], mapOptions);
          //console.log($scope);

          /**
           * Markierungen setzen
           */
          $scope.$watch('markers', function (markers) {
            //console.log(markers);

            // for-Schleife um durch jede Location in markers zu iterieren
            _.forEach(markers, function (marker) {
              //console.log(marker);
              var location;
              location = new maps.LatLng(marker.location.latitude, marker.location.longitude);
              //console.log(location);

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
            var directionsDisplay;
            var directionsService = new maps.DirectionsService();
            //console.log(route);

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
              console.log(waypoints);
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
                //console.log(response);
                directionsDisplay.setDirections(response);
                //console.log(response.routes.length);
                //$scope.trip.distance = response.routes[0].legs[0].distance.value / 1000;
              }
            });
          }, true);
        });
      }
    };
  }]);