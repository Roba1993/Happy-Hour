// Definiere das Modul und seine Abhängigkeiten
angular.module('myApp.map', [
  'ngRoute',
  'ui.map',
  'uiGmapgoogle-maps'
])

    .config(['$routeProvider', function ($routeProvider) {
      $routeProvider.when('/map', {
        templateUrl: 'map/map.html',
        controller: 'MapLoaderCtrl'
      });
    }])

    .config(function(uiGmapGoogleMapApiProvider) {
      uiGmapGoogleMapApiProvider.configure({
        //    key: 'api key here',
        v: '3.17',
        libraries: 'weather,geometry,visualization'
      });
    })

  // Definiere den Controller
    .controller( 'MapLoaderCtrl', MapLoaderCtrl )

function MapLoaderCtrl ( $scope, $timeout, uiGmapGoogleMapApi )
{
  $scope.model = {};
  $scope.route = {};
  $scope.address = 'los angeles';
  $scope.route.pickupLocation = 'los angeles';
  $scope.route.dropoffLocation = 'santa barbara';
  $scope.route.waypoints = [];
  $scope.route.waypoints.push({
    location: 'santa clarita',
    stopover: true
  });
  // choose between DRIVING|BICYCLING|TRANSIT|WALKING
  $scope.route.travelMode = google.maps.DirectionsTravelMode.WALKING;

  // Setze Map in den Scope (mit center auf Stuttgart)
  $scope.model.mapOptions = {
    center: new google.maps.LatLng(48.775556, 9.182778),
    zoom: 14,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };

  $timeout(function(){
    $scope.directionsDisplay = new google.maps.DirectionsRenderer();

    //this is where we pass our the map object to the directionsDisplay.setMap method
    $scope.directionsDisplay.setMap($scope.model.myMap);
    google.maps.event.trigger($scope.model.myMap, 'resize');
  },0);

  $scope.click = function () {
    var geocoder = new google.maps.Geocoder();

    alert("I am going to find your request :)");
    geocoder.geocode({'address': $scope.address}, function (results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        console.log($scope.address);
        console.log(results[0].geometry.location);
        console.log($scope.model.myMap);

        $scope.model.myMap.setCenter(results[0].geometry.location);
        var marker = new google.maps.Marker({
          map: $scope.model.myMap,
          position: results[0].geometry.location
        })
      } else {
        alert(status);
      }
    });
  };

  $scope.calcRoute = function() {
    $scope.directionsService = new google.maps.DirectionsService();

    var request = {
      origin: $scope.route.pickupLocation,
      destination: $scope.route.dropoffLocation,
      waypoints: $scope.route.waypoints,
      travelMode: $scope.route.travelMode
    };

    //call the route method on google map api direction service with the request
    //which returns a response which can be directly provided to
    //directiondisplay object to display the route returned on the map
    $scope.directionsService.route(request, function(response, status) {

      if (status == google.maps.DirectionsStatus.OK) {
        console.log(response);
        $scope.directionsDisplay.setDirections(response);
        console.log(response.routes.length);
        //$scope.trip.distance = response.routes[0].legs[0].distance.value / 1000;

      }
    });
  }

  uiGmapGoogleMapApi.then( function ( maps ) {
    console.log( "loaded" );
    console.log( $scope );
  });
}