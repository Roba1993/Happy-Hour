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
				var lastValue = null;

				// Wenn result bereits definiert ist den Slider auf den Wert einstellen
				$scope.$watch('result', function() {
					// Wenn result definiert ist, und nicht der letzten Änderung entspricht (vermeidet eine unendliche Schleife)
					if($scope.result !== undefined && !_.isEqual($scope.result, lastValue)) {
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
				});

				// Bei einer Werteveränderung
				var valueChanged = function() {
					var value = $input.val();
					// Wenn es sich um einen Doppelslider handelt, Werte in ein Array schreiben
					if($scope.options.type === 'double') {
						value = value.split(';');
					}

					// Nummernstrings in Nummern konvertieren [(+value) transformiert in Nummer]
					if(_.isArray(value)) {
						// Die nachfolgende Funktion wird auf jedes Arrayelement angewendet
						value = _.map(value, function(val) {
							// Wenn das Ergebnis des casts eine valide Zahl ist in diese umwandeln
							if(!_.isNaN(+val)) {
								return +val;
							}
							return val;
						});
					}
					else {
						// Wenn das Ergebnis des casts eine valide Zahl ist in diese umwandeln
						if(!_.isNaN(+value)) {
							value = +value;
						}
					}
					// Wert speichern, um unendlichen $watch-Loop zu vermeiden
					lastValue = value;
					// Scope über die Werteänderung informieren und Wert in result schreiben
					$scope.$apply(function() {
						$scope.result = value;
					});
				};

				slider.options.onChange = valueChanged;
			}
		};
	}]);