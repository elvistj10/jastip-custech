(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferHistoryDeleteController',WithdrawalTransferHistoryDeleteController);

    WithdrawalTransferHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'WithdrawalTransferHistory'];

    function WithdrawalTransferHistoryDeleteController($uibModalInstance, entity, WithdrawalTransferHistory) {
        var vm = this;

        vm.withdrawalTransferHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WithdrawalTransferHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
