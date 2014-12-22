/**
 * Tests die SliderDirective
 * @author Markus Thömmes
 */
describe('SliderDirective', function() {
	var $compile;
	var $rootScope;

	beforeEach(module('happyHour.directives.SliderDirective'));

	beforeEach(inject(function(_$compile_, _$rootScope_){
	  $compile = _$compile_;
	  $rootScope = _$rootScope_;
	}));

    it('initialisiert den Slider mit den durchgegeben Optionen', function() {
    	spyOn($.fn, 'ionRangeSlider').and.callThrough();

		$compile('<input-slider options="{test: 2}" result="test" />')($rootScope);
		$rootScope.$digest();

		expect($.fn.ionRangeSlider).toHaveBeenCalledWith({test: 2});
    });

    it('initialisiert den Slider mit dem "result" Wert', function() {
    	$rootScope.test = 2;
    	var element = $compile('<input-slider options="{min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var slider = element.find('input').data('ionRangeSlider');

		expect(slider.options.from).toBe(2);
    });

    it('initialisiert den Doppelslider mit dem "result" Wert', function() {
    	$rootScope.test = [2, 4];
    	var element = $compile('<input-slider options="{type:\'double\', min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var slider = element.find('input').data('ionRangeSlider');

		expect(slider.options.from).toBe(2);
		expect(slider.options.to).toBe(4);
    });

    it('passt den Slider bei einem geaenderten "result" Wert an', function() {
    	$rootScope.test = 2;
    	var element = $compile('<input-slider options="{min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var slider = element.find('input').data('ionRangeSlider');

		expect(slider.options.from).toBe(2);

		$rootScope.test = 4;
		$rootScope.$digest();

		expect(slider.options.from).toBe(4);
    });

    it('passt den Doppelslider bei einem geaenderten "result" Wert an', function() {
    	$rootScope.test = [2, 4];
    	var element = $compile('<input-slider options="{type:\'double\', min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var slider = element.find('input').data('ionRangeSlider');

		expect(slider.options.from).toBe(2);
		expect(slider.options.to).toBe(4);

		$rootScope.test = [3, 6];
		$rootScope.$digest();
		
		expect(slider.options.from).toBe(3);
		expect(slider.options.to).toBe(6);
    });

    it('passt den "result" Wert durch Aendern des Sliders an', function() {
    	$rootScope.test = 2;
    	var element = $compile('<input-slider options="{min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var $input = element.find('input');
		var slider = $input.data('ionRangeSlider');

		expect($rootScope.test).toBe(2);

		$input.val(4);
		slider.options.onChange();
		expect($rootScope.test).toBe(4);
    });

    it('passt den "result" Wert durch Aendern des Sliders an', function() {
    	$rootScope.test = [2, 4];
    	var element = $compile('<input-slider options="{type:\'double\', min:0.1, max:10, step:0.1, from:1, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var $input = element.find('input');
		var slider = $input.data('ionRangeSlider');

		expect($rootScope.test).toEqual([2, 4]);

		$input.val('4;5');
		slider.options.onChange();
		expect($rootScope.test).toEqual([4, 5]);
    });

    it('funktioniert auch mit einem "value" Parameter', function() {
    	$rootScope.test = ['zwei', 'vier'];
    	var element = $compile('<input-slider options="{type:\'double\', values: [\'eins\', \'zwei\', \'drei\', \'vier\'], from:1, to:2, hide_min_max:true}" result="test" />')($rootScope);
		$rootScope.$digest();

		var $input = element.find('input');
		var slider = $input.data('ionRangeSlider');

		expect($rootScope.test).toEqual(['zwei', 'vier']);

		$input.val('eins;vier');
		slider.options.onChange();
		expect($rootScope.test).toEqual(['eins', 'vier']);
    });
});