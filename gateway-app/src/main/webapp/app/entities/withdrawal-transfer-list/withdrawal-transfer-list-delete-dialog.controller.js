(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferListDeleteController',WithdrawalTransferListDeleteController);

    WithdrawalTransferListDeleteController.$inject = ['$uibModalInstance', 'entity', 'WithdrawalTransferList'];

    function WithdrawalTransferListDeleteController($uibModalInstance, entity, WithdrawalTransferList) {
        var vm = this;

        vm.withdrawalTransferList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WithdrawalTransferList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
