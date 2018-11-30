(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferFailedDetailController', WithdrawalTransferFailedDetailController);

    WithdrawalTransferFailedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WithdrawalTransferFailed'];

    function WithdrawalTransferFailedDetailController($scope, $rootScope, $stateParams, previousState, entity, WithdrawalTransferFailed) {
        var vm = this;

        vm.withdrawalTransferFailed = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:withdrawalTransferFailedUpdate', function(event, result) {
            vm.withdrawalTransferFailed = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
