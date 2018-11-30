(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferHistoryDetailController', WithdrawalTransferHistoryDetailController);

    WithdrawalTransferHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WithdrawalTransferHistory'];

    function WithdrawalTransferHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, WithdrawalTransferHistory) {
        var vm = this;

        vm.withdrawalTransferHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:withdrawalTransferHistoryUpdate', function(event, result) {
            vm.withdrawalTransferHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
