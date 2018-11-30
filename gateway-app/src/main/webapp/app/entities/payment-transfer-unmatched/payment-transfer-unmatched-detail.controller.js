(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferUnmatchedDetailController', PaymentTransferUnmatchedDetailController);

    PaymentTransferUnmatchedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentTransferUnmatched'];

    function PaymentTransferUnmatchedDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentTransferUnmatched) {
        var vm = this;

        vm.paymentTransferUnmatched = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:paymentTransferUnmatchedUpdate', function(event, result) {
            vm.paymentTransferUnmatched = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
