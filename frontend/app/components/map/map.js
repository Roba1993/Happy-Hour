// Definiere das Modul und seine Abhängigkeiten
angular.module('myApp.map', [
    //'ngRoute',
    'ui.map',
    'uiGmapgoogle-maps'
])

    .directive( 'map', function factory ( $log ) {
        var directiveDefinitionObject = {
            restrict: 'E',
            scope: {
                markers: '@',
                route: '@',
                mapOptions: '@',
                myMap: '@'
            },
            template: '<div ui-map="model.myMap" ui-options="model.mapOptions" class="map-canvas"></div>',
            link: function( scope, iElement, iAttrs, controller ) {
                //scope.model.mapOptions = iAttrs.options;
                scope.model.mapOptions = {
                    center: new google.maps.LatLng(48.775556, 9.182778),
                    zoom: 14,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                scope.markers = iAttrs.markers;
                scope.route = iAttrs.route;
            }
        };
        $log.log( 'map directive created' );
        return directiveDefinitionObject;
    })

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/map', {
            templateUrl: '/app/components/map/map.html',
            controller: 'MapLoaderCtrl'
        });
    }])

    .config(function (uiGmapGoogleMapApiProvider) {
        uiGmapGoogleMapApiProvider.configure({
            //    key: 'api key here',
            v: '3.17',
            libraries: 'weather,geometry,visualization'
        });
    })

    // Definiere den Controller
    .controller('MapLoaderCtrl', MapLoaderCtrl);

function MapLoaderCtrl($scope, $timeout, googleMapsApi) {
    //$scope.model = {};
    $scope.route = {};
    $scope.markers = [];
    $scope.address = 'Stuttgart';
    $scope.route.pickupLocation = 'Mauritius Bar-Restaurant-Lounge, Marienstraße 12, Stuttgart';
    $scope.route.dropoffLocation = 'Mata Hari, Geißstraße 3,70173 Stuttgart';
    $scope.route.waypoints = [];
    $scope.route.waypoints.push({
        location: 'Tahiti Bar und Tabledance, Königstraße 51, 70173 Stuttgart',
        stopover: true
    });
    $scope.route.waypoints.push({
        location: 'Kings Club, Calwer Straße 21, 70173 Stuttgart',
        stopover: true
    });
    // choose between DRIVING|BICYCLING|TRANSIT|WALKING
    $scope.route.travelMode = google.maps.DirectionsTravelMode.WALKING;

    // Setze Map in den Scope (mit center auf Stuttgart)
    /*$scope.model.mapOptions = {
        center: new google.maps.LatLng(48.775556, 9.182778),
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };*/

    $timeout(function () {
        $scope.directionsDisplay = new google.maps.DirectionsRenderer();

        //this is where we pass our the map object to the directionsDisplay.setMap method
        $scope.directionsDisplay.setMap($scope.model.myMap);
        google.maps.event.trigger($scope.model.myMap, 'resize');
    }, 0);

    $scope.click = function () {
        var geocoder = new google.maps.Geocoder();

        //alert('I am going to find your request :)');
        geocoder.geocode({'address': $scope.address}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                console.log($scope.address);
                console.log(results[0].geometry.location);
                console.log($scope.model.myMap);

                $scope.model.myMap.setCenter(results[0].geometry.location);
                console.log('adding marker ...');
                $scope.addMarker(results[0].geometry.location);
                $scope.showMarkers();
            } else {
                alert(status);
            }
        });
    };

    $scope.calcRoute = function () {
        $scope.addRoute();
        $scope.directionsService = new google.maps.DirectionsService();

        $scope.route.request = {
            origin: $scope.route.pickupLocation,
            destination: $scope.route.dropoffLocation,
            waypoints: $scope.route.waypoints,
            travelMode: $scope.route.travelMode
        };

        //call the route method on google map api direction service with the request
        //which returns a response which can be directly provided to
        //directiondisplay object to display the route returned on the map
        $scope.directionsService.route($scope.route.request, function (response, status) {

            if (status === google.maps.DirectionsStatus.OK) {
                console.log(response);
                $scope.directionsDisplay.setDirections(response);
                console.log(response.routes.length);
                //$scope.trip.distance = response.routes[0].legs[0].distance.value / 1000;
            }
        });
    };

    // add a marker to the map and push it in the array
    $scope.addMarker = function (location) {
        var marker = new google.maps.Marker({
            map: $scope.model.myMap,
            position: location
        });

        $scope.markers.push(marker);
        console.log('marker added!');
    };

    // show all markers which are stored in the array
    $scope.showMarkers = function () {
        $scope.setAllMap($scope.model.myMap);
    };

    // sets the map on all markers in the array
    $scope.setAllMap = function (map) {
        for (var i = 0; i < $scope.markers.length; i++) {
            $scope.markers[i].setMap(map);
        }
    };

    // deletes all markers on the map and clears the array
    $scope.deleteMarkers = function () {
        $scope.clearMarkers();
        $scope.markers = [];
    };

    // clears map binding from all markers in the marker array
    $scope.clearMarkers = function () {
        $scope.setAllMap(null);
    };

    // add a route to the map
    $scope.addRoute = function () {
        //$scope.directionsDisplay.setMap($scope.model.map);
    };

    // deletes the route
    $scope.deleteRoute = function () {
        console.log('route deleted');
        $scope.directionsDisplay.setMap(null);
    };

    googleMapsApi.then(function () {
        console.log('gmaps api loaded');
        console.log($scope);
    });
}

/*
 function fixInfoWindow() {
 //Here we redefine set() method.
 //If it is called for map option, we hide InfoWindow, if "noSupress" option isnt true.
 //As Google doesn't know about this option, its InfoWindows will not be opened.
 var set = google.maps.InfoWindow.prototype.set;
 google.maps.InfoWindow.prototype.set = function (key, val) {
 if (key === 'map') {
 if (!this.get('noSupress')) {
 console.log('This InfoWindow is supressed. To enable it, set "noSupress" option to true');
 return;
 }
 }
 set.apply(this, arguments);
 };
 }*/
