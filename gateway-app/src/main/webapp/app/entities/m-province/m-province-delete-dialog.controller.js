(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MProvinceDeleteController',MProvinceDeleteController);

    MProvinceDeleteController.$inject = ['$uibModalInstance', 'entity', 'MProvince'];

    function MProvinceDeleteController($uibModalInstance, entity, MProvince) {
        var vm = this;

        vm.mProvince = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MProvince.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
