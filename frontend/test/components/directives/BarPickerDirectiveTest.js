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
    	$rootScope.bars = [{name: 1}, {name: 2}, {name: 3}];
    	$rootScope.test = null;

		var element = $compile('<bar-slider bars="bars" result="test" />')($rootScope);
		$rootScope.$digest();
		var $isoScope = element.isolateScope();
		
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(0);

		$isoScope.nextSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(1);

		$isoScope.nextSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(2);

		$isoScope.nextSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(2);

		$isoScope.previousSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(1);

		$isoScope.previousSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(0);

		$isoScope.previousSlide();
		$rootScope.$digest();
		expect($isoScope.chosenSlide).toBe(0);
    });

    it('befuellt die result Variable bei einer Auswahl', function() {
    	$rootScope.bars = [{name: 1}, {name: 2}, {name: 3}];
    	$rootScope.test = null;

		var element = $compile('<bar-slider bars="bars" result="test" />')($rootScope);
		$rootScope.$digest();
		var $isoScope = element.isolateScope();
		
		$rootScope.$digest();
		expect($rootScope.test).toBe(null);

		$isoScope.slideChosen();
		$rootScope.$digest();
		expect($rootScope.test.name).toBe(1);

		$isoScope.nextSlide();
		$rootScope.$digest();
		expect($rootScope.test.name).toBe(1);

		$isoScope.nextSlide();
		$isoScope.slideChosen();
		$rootScope.$digest();
		expect($rootScope.test.name).toBe(3);
    });

  });