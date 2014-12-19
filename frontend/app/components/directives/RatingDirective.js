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
				// Auf 0.5er Schritte runden
				var value  = Math.round($scope.value*2)/2;

				var strings = [];
				for(var i = 0; i < 5; i++) {
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
				
				// Die Elemente ein- und zusammenfügen
				element[0].innerHTML = strings.join('');
			}
		};
	}]);