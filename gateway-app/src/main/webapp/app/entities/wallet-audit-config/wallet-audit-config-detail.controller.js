(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditConfigDetailController', WalletAuditConfigDetailController);

    WalletAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WalletAuditConfig'];

    function WalletAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, WalletAuditConfig) {
        var vm = this;

        vm.walletAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:walletAuditConfigUpdate', function(event, result) {
            vm.walletAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
