(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletTransactionDetailController', WalletTransactionDetailController);

    WalletTransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WalletTransaction', 'Wallet'];

    function WalletTransactionDetailController($scope, $rootScope, $stateParams, previousState, entity, WalletTransaction, Wallet) {
        var vm = this;

        vm.walletTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:walletTransactionUpdate', function(event, result) {
            vm.walletTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
