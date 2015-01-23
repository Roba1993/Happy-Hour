/**
 * Tests die DayPickerDirective
 * @author Markus Thömmes
 */
describe('DayPickerDirective', function() {
	var $compile;
	var $rootScope;

	beforeEach(module('happyHour.directives.DayPickerDirective'));
	beforeEach(module('happyHour.filters.DaysFilter'));

	beforeEach(inject(function(_$compile_, _$rootScope_){
		$compile = _$compile_;
		$rootScope = _$rootScope_;
	}));

	it('sollte die Steuerelemente generieren', function() {
		var element = $compile('<input-day result="test" />')($rootScope);

		$rootScope.$digest();

		expect(element.children().length).toBe(3);
		expect(element.children().eq(0).text()).toBe('<');
		expect(element.children().eq(2).text()).toBe('>');
	});

	it('sollte auf den heutigen Tag als Standardwert zurueckfallen', function() {
		$compile('<input-day result="test" />')($rootScope);

		$rootScope.$digest();

		if($rootScope.test === 7) {
			$rootScope.test = 0;
		}
		expect($rootScope.test).toBe(new Date().getDay());
	});
	
	it('sollte die Tage auf Klick durchschalten', function() {
		$rootScope.test = 1;
		var element = $compile('<input-day result="test" />')($rootScope);
		var nextHandle = element.children().eq(2);
		var prevHandle = element.children().eq(0);

		$rootScope.$digest();

		expect($rootScope.test).toBe(1);
		nextHandle.click();
		expect($rootScope.test).toBe(2);
		nextHandle.click();
		expect($rootScope.test).toBe(3);
		nextHandle.click();
		expect($rootScope.test).toBe(4);
		nextHandle.click();
		expect($rootScope.test).toBe(5);
		nextHandle.click();
		expect($rootScope.test).toBe(6);
		nextHandle.click();
		expect($rootScope.test).toBe(7);
		nextHandle.click();
		expect($rootScope.test).toBe(1);


		prevHandle.click();
		expect($rootScope.test).toBe(7);
		prevHandle.click();
		expect($rootScope.test).toBe(6);
		prevHandle.click();
		expect($rootScope.test).toBe(5);
		prevHandle.click();
		expect($rootScope.test).toBe(4);
		prevHandle.click();
		expect($rootScope.test).toBe(3);
		prevHandle.click();
		expect($rootScope.test).toBe(2);
		prevHandle.click();
		expect($rootScope.test).toBe(1);
	});

	it('sollte eine lesbare Form des Tages anzeigen', function() {
		$rootScope.test = 1;
		var element = $compile('<input-day result="test" />')($rootScope);

		$rootScope.$digest();

		expect(element.children().eq(1).text()).toBe('Montag');
	});

	it('sollte das Ergebnis auf 2 Wege binden', function() {
		$rootScope.test = 1;
		var element = $compile('<input-day result="test" />')($rootScope);
		$rootScope.$digest();

		expect($rootScope.test).toBe(1);

		$rootScope.test = 3;
		$rootScope.$digest();
		expect(element.children().eq(1).text()).toBe('Mittwoch');

		element.children().eq(2).click();

		expect($rootScope.test).toBe(4);
		expect(element.children().eq(1).text()).toBe('Donnerstag');
	});

  });