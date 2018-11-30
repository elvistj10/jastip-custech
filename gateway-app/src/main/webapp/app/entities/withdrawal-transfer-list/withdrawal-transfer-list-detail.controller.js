(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferListDetailController', WithdrawalTransferListDetailController);

    WithdrawalTransferListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WithdrawalTransferList'];

    function WithdrawalTransferListDetailController($scope, $rootScope, $stateParams, previousState, entity, WithdrawalTransferList) {
        var vm = this;

        vm.withdrawalTransferList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:withdrawalTransferListUpdate', function(event, result) {
            vm.withdrawalTransferList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
