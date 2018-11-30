(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditConfigDeleteController',WalletAuditConfigDeleteController);

    WalletAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'WalletAuditConfig'];

    function WalletAuditConfigDeleteController($uibModalInstance, entity, WalletAuditConfig) {
        var vm = this;

        vm.walletAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WalletAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
