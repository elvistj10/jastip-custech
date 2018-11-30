(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditDetailController', WalletAuditDetailController);

    WalletAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WalletAudit'];

    function WalletAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, WalletAudit) {
        var vm = this;

        vm.walletAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:walletAuditUpdate', function(event, result) {
            vm.walletAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
