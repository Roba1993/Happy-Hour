describe('happyHour.persistence.RoutesPersistence module', function() {

  beforeEach(module('happyHour.persistence.RoutesPersistence'));

  describe('RoutesPersistenceService', function(){

    it('sollte Routen hinzufügen', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();
    }));

  });
});