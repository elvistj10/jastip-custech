(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletDetailController', WalletDetailController);

    WalletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Wallet', 'WalletTransaction'];

    function WalletDetailController($scope, $rootScope, $stateParams, previousState, entity, Wallet, WalletTransaction) {
        var vm = this;

        vm.wallet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:walletUpdate', function(event, result) {
            vm.wallet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
