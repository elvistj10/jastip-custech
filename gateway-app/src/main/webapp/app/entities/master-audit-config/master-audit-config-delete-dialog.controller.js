(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditConfigDeleteController',MasterAuditConfigDeleteController);

    MasterAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'MasterAuditConfig'];

    function MasterAuditConfigDeleteController($uibModalInstance, entity, MasterAuditConfig) {
        var vm = this;

        vm.masterAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MasterAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
