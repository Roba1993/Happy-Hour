/**
 * Tests die BarPickerDirective
 * @author Markus Thömmes
 */
describe('BarPickerDirective', function() {
	var $compile;
	var $rootScope;

	beforeEach(module('happyHour.templates'));
	beforeEach(module('happyHour.directives.BarPickerDirective'));

	beforeEach(inject(function(_$compile_, _$rootScope_){
	  $compile = _$compile_;
	  $rootScope = _$rootScope_;
	}));

    it('sollte die Liste an Elementen generieren', function() {
    	$rootScope.bars = [{}, {}];
		var element = $compile('<bar-slider bars="bars" />')($rootScope);

		$rootScope.$digest();
		expect(element.children().eq(0).children().length).toBe(2);

		$rootScope.bars = [{}];
		$rootScope.$digest();
		expect(element.children().eq(0).children().length).toBe(1);
    });

    it('slidet per swipe die Liste durch', function() {
    	$rootScope.bars = [{name: 1}, {name: 2}];
    	$rootScope.result = null;
		var element = $compile('<bar-slider bars="bars" result="result" />')($rootScope);

		$rootScope.$digest();
		element.children().eq(0).trigger('swipeleft');
		$rootScope.$digest();
		console.log($rootScope.result);
    });

  });