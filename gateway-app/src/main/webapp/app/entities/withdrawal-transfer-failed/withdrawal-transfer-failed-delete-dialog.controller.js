(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferFailedDeleteController',WithdrawalTransferFailedDeleteController);

    WithdrawalTransferFailedDeleteController.$inject = ['$uibModalInstance', 'entity', 'WithdrawalTransferFailed'];

    function WithdrawalTransferFailedDeleteController($uibModalInstance, entity, WithdrawalTransferFailed) {
        var vm = this;

        vm.withdrawalTransferFailed = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WithdrawalTransferFailed.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
