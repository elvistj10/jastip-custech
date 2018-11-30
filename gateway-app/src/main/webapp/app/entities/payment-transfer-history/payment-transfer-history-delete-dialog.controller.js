(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferHistoryDeleteController',PaymentTransferHistoryDeleteController);

    PaymentTransferHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentTransferHistory'];

    function PaymentTransferHistoryDeleteController($uibModalInstance, entity, PaymentTransferHistory) {
        var vm = this;

        vm.paymentTransferHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentTransferHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
