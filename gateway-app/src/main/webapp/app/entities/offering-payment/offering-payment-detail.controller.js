(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPaymentDetailController', OfferingPaymentDetailController);

    OfferingPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfferingPayment', 'Offering'];

    function OfferingPaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, OfferingPayment, Offering) {
        var vm = this;

        vm.offeringPayment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringPaymentUpdate', function(event, result) {
            vm.offeringPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
