(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditDeleteController',TransactionAuditDeleteController);

    TransactionAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransactionAudit'];

    function TransactionAuditDeleteController($uibModalInstance, entity, TransactionAudit) {
        var vm = this;

        vm.transactionAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransactionAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
