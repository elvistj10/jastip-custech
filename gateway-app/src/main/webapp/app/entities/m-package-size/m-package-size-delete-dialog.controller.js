(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MPackageSizeDeleteController',MPackageSizeDeleteController);

    MPackageSizeDeleteController.$inject = ['$uibModalInstance', 'entity', 'MPackageSize'];

    function MPackageSizeDeleteController($uibModalInstance, entity, MPackageSize) {
        var vm = this;

        vm.mPackageSize = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MPackageSize.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
