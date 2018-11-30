'use strict';

describe('Controller Tests', function() {

    describe('Offering Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOffering, MockPosting, MockOfferingDeliveryInfo, MockOfferingDeliveryService, MockOfferingPayment, MockOfferingPuchase, MockOfferingCourier;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOffering = jasmine.createSpy('MockOffering');
            MockPosting = jasmine.createSpy('MockPosting');
            MockOfferingDeliveryInfo = jasmine.createSpy('MockOfferingDeliveryInfo');
            MockOfferingDeliveryService = jasmine.createSpy('MockOfferingDeliveryService');
            MockOfferingPayment = jasmine.createSpy('MockOfferingPayment');
            MockOfferingPuchase = jasmine.createSpy('MockOfferingPuchase');
            MockOfferingCourier = jasmine.createSpy('MockOfferingCourier');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Offering': MockOffering,
                'Posting': MockPosting,
                'OfferingDeliveryInfo': MockOfferingDeliveryInfo,
                'OfferingDeliveryService': MockOfferingDeliveryService,
                'OfferingPayment': MockOfferingPayment,
                'OfferingPuchase': MockOfferingPuchase,
                'OfferingCourier': MockOfferingCourier
            };
            createController = function() {
                $injector.get('$controller')("OfferingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gatewayApp:offeringUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
