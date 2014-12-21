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
					var options = {
						componentRestrictions: { country: 'de' }
					};
					var api = new maps.places.Autocomplete(element.children()[0], options);

					maps.event.addListener(api, 'place_changed', function() {
						var place = api.getPlace();
						$scope.$apply(function() {
							$scope.result = place.geometry.location;
						});
					});
				});
			}
		};
	}]);