(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCityDeleteController',MCityDeleteController);

    MCityDeleteController.$inject = ['$uibModalInstance', 'entity', 'MCity'];

    function MCityDeleteController($uibModalInstance, entity, MCity) {
        var vm = this;

        vm.mCity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MCity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
