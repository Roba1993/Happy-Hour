/**
 * Stellt ein `input`-Element mit Anbindung an die Google Maps Places Autocomplete API bereit
 *
 * @author Markus Thömmes
 */
angular.module('happyHour.directives.PlacesAutocompleteDirective', [])
	.directive('inputPlaces', ['MapLoaderService', function(MapLoaderService) {
		return {
			restrict: 'E',
			scope: {
				/**
				 * Die Variable in dem die Auswahl gespeichert wird
				 * @type {Location}
				 */
				result: '='
			},
			template: '<input type="text" />',
			link: function ($scope, element) {
				MapLoaderService.then(function(maps) {
					var input = element.children()[0];

					// Bei jeder Änderung der result Variable die Lokation reverse Geocoden
					var geocoder = new maps.Geocoder();
					$scope.$watch('result', function(location) {
						if($scope.result) {
							// Das Location-Objekt in einer google.maps.LatLng-Objekt umwandeln
							var googleLocation = new maps.LatLng(location.latitude, location.longitude);
							geocoder.geocode({latLng: googleLocation}, function(result, status) {
								if (status === maps.GeocoderStatus.OK) {
									input.value = result[1].formatted_address;
								}
							});
						}
					}, true);

					// Google Maps Autocomplete initialisieren
					var options = {
						componentRestrictions: {country: 'de'}
					};
					var api = new maps.places.Autocomplete(input, options);

					// Wenn der Nutzer einen Platz auswählt
					maps.event.addListener(api, 'place_changed', function() {
						var place = api.getPlace();
						// Ein Location-Objekt in das result setzen und den $scope über die Änderung informieren
						$scope.$apply(function() {
							$scope.result = {latitude: place.geometry.location.lat(), longitude: place.geometry.location.lng()};
						});
					});
				});
			}
		};
	}]);