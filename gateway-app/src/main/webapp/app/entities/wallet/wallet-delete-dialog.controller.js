(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletDeleteController',WalletDeleteController);

    WalletDeleteController.$inject = ['$uibModalInstance', 'entity', 'Wallet'];

    function WalletDeleteController($uibModalInstance, entity, Wallet) {
        var vm = this;

        vm.wallet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Wallet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
