/**
 * Tests fuer den TimeFilter Filter
 * @author Dorothee Nies
 */
describe('Timefilter', function() {
  beforeEach(function() {


    module('happyHour.filters.TimeFilter');

  });

    it('sollte die Start- und Endzeit korrekt wiedergeben', inject(function(formatTimeFilter) {
		expect(formatTimeFilter({startTime:'14:00', endTime:'18:30'})).toBe('14:00 - 18:30');
	
    }));
	
	it('sollte nur die Anfangszeit wiedergeben', inject(function(formatTimeFilter) {
		expect(formatTimeFilter({startTime:'14:00', endTime:null})).toBe('ab 14:00');
	
    }));
	
	it('sollte einen leeren String wiedergeben', inject(function(formatTimeFilter) {
		expect(formatTimeFilter({startTime:null, endTime:null})).toBe('');
		expect(formatTimeFilter({startTime:null, endTime:'15:00'})).toBe('');	
    }));

	
	

  });