(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PackageSizeDeleteController',PackageSizeDeleteController);

    PackageSizeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PackageSize'];

    function PackageSizeDeleteController($uibModalInstance, entity, PackageSize) {
        var vm = this;

        vm.packageSize = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PackageSize.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
