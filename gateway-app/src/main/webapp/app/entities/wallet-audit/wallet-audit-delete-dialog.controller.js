(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditDeleteController',WalletAuditDeleteController);

    WalletAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'WalletAudit'];

    function WalletAuditDeleteController($uibModalInstance, entity, WalletAudit) {
        var vm = this;

        vm.walletAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WalletAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
