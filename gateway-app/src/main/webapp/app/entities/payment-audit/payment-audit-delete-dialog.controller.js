(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditDeleteController',PaymentAuditDeleteController);

    PaymentAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentAudit'];

    function PaymentAuditDeleteController($uibModalInstance, entity, PaymentAudit) {
        var vm = this;

        vm.paymentAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
