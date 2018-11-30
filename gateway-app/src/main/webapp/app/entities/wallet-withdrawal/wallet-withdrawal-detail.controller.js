(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletWithdrawalDetailController', WalletWithdrawalDetailController);

    WalletWithdrawalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WalletWithdrawal'];

    function WalletWithdrawalDetailController($scope, $rootScope, $stateParams, previousState, entity, WalletWithdrawal) {
        var vm = this;

        vm.walletWithdrawal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:walletWithdrawalUpdate', function(event, result) {
            vm.walletWithdrawal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
