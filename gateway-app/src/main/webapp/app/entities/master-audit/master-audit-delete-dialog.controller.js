(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditDeleteController',MasterAuditDeleteController);

    MasterAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'MasterAudit'];

    function MasterAuditDeleteController($uibModalInstance, entity, MasterAudit) {
        var vm = this;

        vm.masterAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MasterAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
