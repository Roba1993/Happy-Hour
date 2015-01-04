/**
 * Tests fuer den DaysFilter Filter
 * @author Dorothee Nies
 */
describe('DaysFilter', function() {
  beforeEach(function() {


    module('happyHour.filters.DaysFilter');

  });

    it('sollte die Wochentage korrekt wiedergeben', inject(function(formatDaysFilter) {
		expect(formatDaysFilter([2,5,7])).toBe('Di/Fr/So');
		expect(formatDaysFilter([1,2,3,4,5,6,7])).toBe('Jeden Tag');
    }));
	
	it('sollte einen leeren String zurueckgeben', inject(function(formatDaysFilter) {
		expect(formatDaysFilter([])).toBe('');
    }));
	
	it('sollte nur die vorhandenen Tage zurueckgeben', inject(function(formatDaysFilter) {
		expect(formatDaysFilter([1,8,9])).toBe('Mo');
		expect(formatDaysFilter([0])).toBe('');
    }));
	
	it('sollte die Tage in Langform zurueckgeben', inject(function(formatDaysFilter) {
		expect(formatDaysFilter([1,5],true)).toBe('Montag/Freitag');
    }));
	
	it('sollte die Tage in korrekt zurueckgeben', inject(function(formatDaysFilter) {
		expect(formatDaysFilter(1,true)).toBe('Montag');
		expect(formatDaysFilter(3)).toBe('Mi');
    }));


  });