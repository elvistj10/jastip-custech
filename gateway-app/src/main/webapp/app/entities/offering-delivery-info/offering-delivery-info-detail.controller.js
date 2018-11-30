(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryInfoDetailController', OfferingDeliveryInfoDetailController);

    OfferingDeliveryInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfferingDeliveryInfo', 'Offering'];

    function OfferingDeliveryInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, OfferingDeliveryInfo, Offering) {
        var vm = this;

        vm.offeringDeliveryInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringDeliveryInfoUpdate', function(event, result) {
            vm.offeringDeliveryInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
