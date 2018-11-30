'use strict';

describe('Controller Tests', function() {

    describe('OfferingDeliveryService Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOfferingDeliveryService, MockOffering;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOfferingDeliveryService = jasmine.createSpy('MockOfferingDeliveryService');
            MockOffering = jasmine.createSpy('MockOffering');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OfferingDeliveryService': MockOfferingDeliveryService,
                'Offering': MockOffering
            };
            createController = function() {
                $injector.get('$controller')("OfferingDeliveryServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gatewayApp:offeringDeliveryServiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
