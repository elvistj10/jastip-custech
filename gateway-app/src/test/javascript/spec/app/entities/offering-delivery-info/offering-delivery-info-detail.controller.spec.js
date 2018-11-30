'use strict';

describe('Controller Tests', function() {

    describe('OfferingDeliveryInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOfferingDeliveryInfo, MockOffering;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOfferingDeliveryInfo = jasmine.createSpy('MockOfferingDeliveryInfo');
            MockOffering = jasmine.createSpy('MockOffering');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'OfferingDeliveryInfo': MockOfferingDeliveryInfo,
                'Offering': MockOffering
            };
            createController = function() {
                $injector.get('$controller')("OfferingDeliveryInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gatewayApp:offeringDeliveryInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
