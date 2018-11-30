(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryServiceDetailController', OfferingDeliveryServiceDetailController);

    OfferingDeliveryServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfferingDeliveryService', 'Offering'];

    function OfferingDeliveryServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, OfferingDeliveryService, Offering) {
        var vm = this;

        vm.offeringDeliveryService = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringDeliveryServiceUpdate', function(event, result) {
            vm.offeringDeliveryService = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
