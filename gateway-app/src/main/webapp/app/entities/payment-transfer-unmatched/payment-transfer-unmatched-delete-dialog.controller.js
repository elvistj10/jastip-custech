(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferUnmatchedDeleteController',PaymentTransferUnmatchedDeleteController);

    PaymentTransferUnmatchedDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentTransferUnmatched'];

    function PaymentTransferUnmatchedDeleteController($uibModalInstance, entity, PaymentTransferUnmatched) {
        var vm = this;

        vm.paymentTransferUnmatched = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentTransferUnmatched.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
