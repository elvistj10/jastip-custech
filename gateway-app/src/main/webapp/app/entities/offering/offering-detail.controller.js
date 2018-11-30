(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDetailController', OfferingDetailController);

    OfferingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offering', 'Posting', 'OfferingDeliveryInfo', 'OfferingDeliveryService', 'OfferingPayment', 'OfferingPuchase', 'OfferingCourier'];

    function OfferingDetailController($scope, $rootScope, $stateParams, previousState, entity, Offering, Posting, OfferingDeliveryInfo, OfferingDeliveryService, OfferingPayment, OfferingPuchase, OfferingCourier) {
        var vm = this;

        vm.offering = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringUpdate', function(event, result) {
            vm.offering = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
