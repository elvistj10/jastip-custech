(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletTransactionDeleteController',WalletTransactionDeleteController);

    WalletTransactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'WalletTransaction'];

    function WalletTransactionDeleteController($uibModalInstance, entity, WalletTransaction) {
        var vm = this;

        vm.walletTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WalletTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
