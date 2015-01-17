/**
 * Stellt eine Bewertung mit Symbolen dar.
 *
 * @author Dorothee Nies
 */
angular.module('happyHour.directives.RatingDirective', [])
	.directive('rating', [function() {
		return {
			restrict: 'E',
			scope: {
				/**
				 * Anzeigeart der Symbole (euro|star)
				 * @type {String}
				 */
				type: '=',
				/**
				 * Anzuzeigende Bewertung
				 * @type {Float}
				 */
				value: '='     
			},
			template: '',
			link: function ($scope, element) {
				$scope.$watch('value + type', function(){
					var strings = [];
					if($scope.value !== -1) {
						var starRating = $scope.value;
						// Sternebewertung kommt zwischen 0 und 10, es sind allerdings nur 5 Icons vorhanden
						if($scope.type === 'star') {
							starRating = starRating/2;
						}

						// Auf 0.5er Schritte runden
						var value  = Math.round(starRating*2)/2;

						var maxIcons = 5;
						if($scope.type === 'euro') {
							maxIcons = 4;
						}

						for(var i = 0; i < maxIcons; i++) {
							// Wenn nur noch ein halbes Icon fehlt, halbes Icon hinzufügen
							if(i + 0.5 === value) {
								strings.push('<span class="i__' + $scope.type + '-halfcolored"></span>');
							}
							// Auffüllen mit vollen Icons
							else if(i < value){
								strings.push('<span class="i__' + $scope.type + '-colored"></span>');
							}
							// Auffüllen mit leeren Icons
							else {
								strings.push('<span class="i__' + $scope.type + '"></span>');
							}
						}
						
						// Textuelle Bewertung hinzufügen
						if($scope.type === 'star') {
							strings.push('<span class="e__rating-description">('+$scope.value+'/10)</span>');
						}
					}
					else {
						strings.push('<span class="e__rating-description">Keine Bewertung</span>');
					}

					// Die Elemente ein- und zusammenfügen
					element[0].innerHTML = strings.join('');
				});
			}
		};
	}]);