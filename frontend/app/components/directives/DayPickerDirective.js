/**
 * Ermöglicht das Auswählen eines Wochentags
 *
 * @author Markus Thömmes
 */
angular.module('happyHour.directives.DayPickerDirective', [])
	.directive('inputDay', [function() {
		return {
			restrict: 'E',
			scope: {
				/**
				 * Die Variable in dem die Auswahl gespeichert wird
				 * @type {Integer}
				 */
				result: '='
			},
			template: '<div ng-click="previousClicked()" class="col-1"><h4>&lt;</h4></div><div class="col-10" style="text-align:center;"><h4 style="color: #fff">{{result | formatDays:true}}</h4></div><div ng-click="nextClicked()" class="col-1" style="text-align:right;"><h4>&gt;</h4></div>',
			link: function ($scope) {
				// Wenn kein Wert vorgesetzt ist, den heutigen Tag als Standard setzen
				if($scope.result === undefined) {
					$scope.result = new Date().getDay();
					if($scope.result === 0) {
						$scope.result = 7;
					}
				}

				$scope.previousClicked = function() {
					// result im Raum 1-7 (Wochentage) halten
					if($scope.result > 1) {
						$scope.result--;
					}
					else {
						$scope.result = 7;
					}
				};

				$scope.nextClicked = function() {
					// result im Raum 1-7 (Wochentage) halten
					if($scope.result < 7) {
						$scope.result++;
					}
					else {
						$scope.result = 1;
					}
				};
			}
		};
	}]);