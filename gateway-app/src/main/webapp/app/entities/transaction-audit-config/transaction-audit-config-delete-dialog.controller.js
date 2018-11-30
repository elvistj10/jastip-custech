(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditConfigDeleteController',TransactionAuditConfigDeleteController);

    TransactionAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransactionAuditConfig'];

    function TransactionAuditConfigDeleteController($uibModalInstance, entity, TransactionAuditConfig) {
        var vm = this;

        vm.transactionAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransactionAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
