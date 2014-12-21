/**
 * Ermöglicht das Auswählen eines Wertes mithilfe eines Sliders
 *
 * @author Markus Thömmes
 */
angular.module('happyHour.directives.SliderDirective', [])
	.directive('inputSlider', [function() {
		return {
			restrict: 'E',
			scope: {
				/**
				 * Die Variable in dem die Auswahl gespeichert wird
				 * @type {*|*[]}
				 */
				result: '=',
				/**
				 * Enthält die Optionen für den Slider
				 * @type {Object}
				 */
				options: '='
			},
			template: '<input type="text" />',
			link: function ($scope, element) {
				var $input = element.children().eq(0);
				// Slider initialisieren
				$input.ionRangeSlider($scope.options);
				var slider = $input.data('ionRangeSlider');

				// Wenn result bereits definiert ist den Slider auf den Wert einstellen
				if($scope.result !== undefined) {
					// Wenn es sich um einen Doppelslider handelt, from und to Wert setzen
					if($scope.options.type === 'double') {
						var from = $scope.result[0];
						var to = $scope.result[1];

						// Wenn manuelle values gesetzt sind, die Indizes der result-Werte als from und to Wert setzen
						// (ionRangeSlider arbeitet hier nur mit Indizes)
						if($scope.options.values) {
							from = _.indexOf($scope.options.values, from);
							to = _.indexOf($scope.options.values, to);
						}
						slider.update({from: from, to: to});
					}
					else {
						var def = $scope.result;

						// Wenn manuelle values gesetzt sind, die Indizes der result-Werte als from und to Wert setzen
						// (ionRangeSlider arbeitet hier nur mit Indizes)
						if($scope.options.values) {
							def = _.indexOf($scope.options.values, def);
						}
						slider.update({from: def});
					}
				}

				// Bei einer Werteveränderung
				$input.on('change', function() {
					var value = $input.val();
					// Wenn es sich um einen Doppelslider handelt, Werte in ein Array schreiben
					if($scope.options.type === 'double') {
						value = value.split(';');
					}
					console.log(value);
					// Scope über die Werteänderung informieren und Wert in result schreiben
					$scope.$apply(function() {
						$scope.result = value;
					});
				});
			}
		};
	}]);