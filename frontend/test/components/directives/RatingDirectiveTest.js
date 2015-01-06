/**
 * Tests die RatingDirective
 * @author Dorothee Nies
 */
describe('RatingDirective', function() {
	var $compile;
	var $rootScope;

	beforeEach(module('happyHour.directives.RatingDirective'));

	beforeEach(inject(function(_$compile_, _$rootScope_){
	  $compile = _$compile_;
	  $rootScope = _$rootScope_;
	}));
	
	it('sollte die Icons korrekt generieren', function() {
		var element = $compile('<rating type="\'star\'" value="test">')($rootScope);

		$rootScope.test = 3;
		$rootScope.$digest();

		expect(element.children().length).toBe(5);
		expect(element.children().eq(0).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(1).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(2).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(3).hasClass('i__star')).toBe(true);
		expect(element.children().eq(4).hasClass('i__star')).toBe(true);
		
		$rootScope.test = 0;
		$rootScope.$digest();
		expect(element.children().eq(0).hasClass('i__star')).toBe(true);
		expect(element.children().eq(1).hasClass('i__star')).toBe(true);
		expect(element.children().eq(2).hasClass('i__star')).toBe(true);
		expect(element.children().eq(3).hasClass('i__star')).toBe(true);
		expect(element.children().eq(4).hasClass('i__star')).toBe(true);
		
		$rootScope.test = 5;
		$rootScope.$digest();
		expect(element.children().eq(0).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(1).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(2).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(3).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(4).hasClass('i__star-colored')).toBe(true);
		
		$rootScope.test = 1.5;
		$rootScope.$digest();
		expect(element.children().eq(0).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(1).hasClass('i__star-halfcolored')).toBe(true);
		expect(element.children().eq(2).hasClass('i__star')).toBe(true);
		expect(element.children().eq(3).hasClass('i__star')).toBe(true);
		expect(element.children().eq(4).hasClass('i__star')).toBe(true);
		
		$rootScope.test = 3.7;
		$rootScope.$digest();
		expect(element.children().eq(0).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(1).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(2).hasClass('i__star-colored')).toBe(true);
		expect(element.children().eq(3).hasClass('i__star-halfcolored')).toBe(true);
		expect(element.children().eq(4).hasClass('i__star')).toBe(true);
    });
	
});