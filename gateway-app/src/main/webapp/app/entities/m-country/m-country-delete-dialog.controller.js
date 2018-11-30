(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCountryDeleteController',MCountryDeleteController);

    MCountryDeleteController.$inject = ['$uibModalInstance', 'entity', 'MCountry'];

    function MCountryDeleteController($uibModalInstance, entity, MCountry) {
        var vm = this;

        vm.mCountry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MCountry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
