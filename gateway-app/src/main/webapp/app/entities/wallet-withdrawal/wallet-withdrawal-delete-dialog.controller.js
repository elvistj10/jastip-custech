(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletWithdrawalDeleteController',WalletWithdrawalDeleteController);

    WalletWithdrawalDeleteController.$inject = ['$uibModalInstance', 'entity', 'WalletWithdrawal'];

    function WalletWithdrawalDeleteController($uibModalInstance, entity, WalletWithdrawal) {
        var vm = this;

        vm.walletWithdrawal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WalletWithdrawal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
