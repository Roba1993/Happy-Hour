angular.module('happyHour.directives.RatingDirective', [])
	.directive('rating', [function() {
		return {
			restrict: 'E',
			scope: {
				type: '=',
				value: '='     
			},
			template: '',
			link: function ($scope, element) {
				var value  = Math.round($scope.value*2)/2;
				var strings = [];
				
				for(var i = 0; i < 5; i++){
					if (i + 0.5 === value){
						strings.push('<span class="i__' + $scope.type + '-halfcolored"></span>');
					}
					else if(i < value){
						strings.push('<span class="i__' + $scope.type + '-colored"></span>');
					}
					else{
						strings.push('<span class="i__' + $scope.type + '"></span>');
					}
				}
				
				element[0].innerHTML = strings.join('');
			}
		};
	}]);