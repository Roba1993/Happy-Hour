/**
 * Ermöglicht das Auswählen einer Bar mithilfe von Swipes nach links und rechts.
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
				 * Die Variable in der die Auswahl gespeichert wird
				 * @type {Bar}
				 */
				result: '='
			},
			templateUrl: 'components/directives/BarPickerTemplate.html',
			link: function ($scope) {
				$scope.chosenSlide = 0;

				$scope.previousSlide = function() {
					// chosenSlide im Rahmen des Arrays (0 - array.length) halten
					if($scope.chosenSlide > 0) {
						$scope.chosenSlide--;
					}
				};

				$scope.nextSlide = function() {
					// chosenSlide im Rahmen des Arrays (0 - array.length) halten
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