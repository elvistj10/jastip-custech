(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferCheckListDeleteController',PaymentTransferCheckListDeleteController);

    PaymentTransferCheckListDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentTransferCheckList'];

    function PaymentTransferCheckListDeleteController($uibModalInstance, entity, PaymentTransferCheckList) {
        var vm = this;

        vm.paymentTransferCheckList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentTransferCheckList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
