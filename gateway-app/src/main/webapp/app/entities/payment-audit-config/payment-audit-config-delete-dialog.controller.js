(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditConfigDeleteController',PaymentAuditConfigDeleteController);

    PaymentAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentAuditConfig'];

    function PaymentAuditConfigDeleteController($uibModalInstance, entity, PaymentAuditConfig) {
        var vm = this;

        vm.paymentAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
