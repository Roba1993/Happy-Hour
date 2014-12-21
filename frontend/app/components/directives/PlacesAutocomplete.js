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
				 * @type {Object}
				 */
				result: '=',
				/**
				 * Die Optionen des Autocomplete Felds
				 * @type {Object}
				 */
				options: '='
			},
			template: '<input type="text" />',
			link: function ($scope, element) {
				MapLoaderService.then(function(maps) {
					var input = element.children()[0];

					// Eine gesetzte Location reverse Geocodieren
					var geocoder = new maps.Geocoder();
					$scope.$watch('result', function(location) {
						var googleLocation = new maps.LatLng(location.latitude, location.longitude);
						geocoder.geocode({latLng: googleLocation}, function(result, status) {
							if (status === maps.GeocoderStatus.OK) {
								input.value = result[1].formatted_address;
							}
						});
					});

					var options = {
						componentRestrictions: {country: 'de'}
					};
					var api = new maps.places.Autocomplete(input, options);

					maps.event.addListener(api, 'place_changed', function() {
						var place = api.getPlace();
						$scope.$apply(function() {
							$scope.result = {latitude: place.geometry.location.lat(), longitude: place.geometry.location.lng()};
						});
					});
				});
			}
		};
	}]);