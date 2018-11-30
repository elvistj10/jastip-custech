(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferHistoryDetailController', PaymentTransferHistoryDetailController);

    PaymentTransferHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentTransferHistory'];

    function PaymentTransferHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentTransferHistory) {
        var vm = this;

        vm.paymentTransferHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:paymentTransferHistoryUpdate', function(event, result) {
            vm.paymentTransferHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
