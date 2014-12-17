/**
 * Die map Direktive ist das Herzstück der Kartenschnittstelle und wird über ein map-HTML Element eingebunden. 
 * Sie erzeugt alle notwendigen HTML-Elemente zum Anzeigen der Karte.
 *
 * @author Denis Beck
 */
angular.module('happyHour.map.MapDirective', ['happyHour.map.MapLoader'])
	.directive('map', ['MapLoaderService', function(MapLoaderService) {
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
			template: '<div class="map-canvas"></div>',
			link: function ($scope, element) {
				MapLoaderService.then(function(maps) {
					var mapOptions = {
						//center: new maps.LatLng($scope.options.center.latitude, $scope.options.center.longitude),
						center: new maps.LatLng(48.775556, 9.182778),
						zoom: 14,
						mapTypeId: google.maps.MapTypeId.ROADMAP
					};
					new maps.Map(element.children()[0], mapOptions);
					//console.log($scope);

					/**
					 * Markierungen setzen
					 */
					$scope.$watch('markers', function(markers) {
						console.log(markers);
						var location;
						location = new maps.LatLng(48.775556, 9.182778);
						/*var marker = new maps.Marker({
							map: $scope.model.myMap,
							position: location
						});*/
					}, true);

					/**
					 * Route setzen
					 */
					$scope.$watch('route', function(route) {
						console.log(route);
					}, true);
				});

				//console.log(attributes);
			}
		};
	}]);