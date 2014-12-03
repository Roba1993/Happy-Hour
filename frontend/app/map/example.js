/**
 * Created by Denis on 01.12.2014.
 */

angular.module('mean').controller('EditTripCtrl', ['$scope', '$modalInstance', '$timeout', 'Trip', function ($scope, $modalInstance, $timeout, Trip) {
    $scope.trip = {};
    $scope.routes = [];
    angular.copy(Trip, $scope.trip);
    //modal events cancel and ok
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.ok = function () {
        $modalInstance.close($scope.trip);
    };
    var directionsDisplay;
    $scope.initializeMapControls = function () {


        $scope.data = {};
        $scope.data.mapOptions = {
            center: new google.maps.LatLng(21.1500, 79.0900),
            zoom: 4,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        $timeout(function () {
            directionsDisplay = new google.maps.DirectionsRenderer();

            //this is where we pass our the map object to the directionsDisplay.setMap method
            directionsDisplay.setMap($scope.data.myMap);
            google.maps.event.trigger($scope.data.myMap, 'resize');
        }, 0);
    }
    $scope.findPath = function () {

        //using the direction service of google map api
        $scope.directionsService = new google.maps.DirectionsService();

        var request = {
            origin: $scope.trip.pickupLocation,
            destination: $scope.trip.dropOfflocation,
            travelMode: google.maps.DirectionsTravelMode.DRIVING
        };
        //call the route method on google map api direction service with the request
        //which returns a response which can be directly provided to
        //directiondisplay object to display the route returned on the map
        $scope.directionsService.route(request, function (response, status) {

            if (status == google.maps.DirectionsStatus.OK) {
                console.log(response);
                directionsDisplay.setDirections(response);
                console.log(response.routes.length);
                $scope.trip.distance = response.routes[0].legs[0].distance.value / 1000;

            }


        });


    }

}

]);