/**
 * Ermöglicht das Auswählen einer Bar
 *
 * @author Markus Thömmes
 */
angular.module('happyHour.directives.BarPickerDirective', [])
	.directive('barSlider', [function() {
		return {
			restrict: 'E',
			scope: {
				/**
				 * Die Bars, die zur Auswahl stehen
				 * @type {Bar[]}
				 */
				bars: '=',
				/**
				 * Die Variable in dem die Auswahl gespeichert wird
				 * @type {Bar}
				 */
				result: '='
			},
			templateUrl: 'components/directives/BarPickerTemplate.html',
			link: function ($scope) {
				$scope.chosenSlide = 0;

				$scope.previousSlide = function() {
					if($scope.chosenSlide > 0) {
						$scope.chosenSlide--;
					}
				};

				$scope.nextSlide = function() {
					if($scope.chosenSlide < $scope.bars.length-1) {
						$scope.chosenSlide++;
					}
				};

				$scope.slideChosen = function() {
					$scope.result = $scope.bars[$scope.chosenSlide];
				};
			}
		};
	}]);