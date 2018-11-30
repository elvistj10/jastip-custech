(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferCheckListDetailController', PaymentTransferCheckListDetailController);

    PaymentTransferCheckListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentTransferCheckList'];

    function PaymentTransferCheckListDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentTransferCheckList) {
        var vm = this;

        vm.paymentTransferCheckList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:paymentTransferCheckListUpdate', function(event, result) {
            vm.paymentTransferCheckList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
