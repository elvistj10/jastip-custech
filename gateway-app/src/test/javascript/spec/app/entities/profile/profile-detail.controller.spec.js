'use strict';

describe('Controller Tests', function() {

    describe('Profile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProfile, MockAddress, MockBankAccount, MockCreditCard, MockFollowingList, MockFollowerList, MockBlockList, MockBlockedByList, MockFeedback;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProfile = jasmine.createSpy('MockProfile');
            MockAddress = jasmine.createSpy('MockAddress');
            MockBankAccount = jasmine.createSpy('MockBankAccount');
            MockCreditCard = jasmine.createSpy('MockCreditCard');
            MockFollowingList = jasmine.createSpy('MockFollowingList');
            MockFollowerList = jasmine.createSpy('MockFollowerList');
            MockBlockList = jasmine.createSpy('MockBlockList');
            MockBlockedByList = jasmine.createSpy('MockBlockedByList');
            MockFeedback = jasmine.createSpy('MockFeedback');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Profile': MockProfile,
                'Address': MockAddress,
                'BankAccount': MockBankAccount,
                'CreditCard': MockCreditCard,
                'FollowingList': MockFollowingList,
                'FollowerList': MockFollowerList,
                'BlockList': MockBlockList,
                'BlockedByList': MockBlockedByList,
                'Feedback': MockFeedback
            };
            createController = function() {
                $injector.get('$controller')("ProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gatewayApp:profileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
