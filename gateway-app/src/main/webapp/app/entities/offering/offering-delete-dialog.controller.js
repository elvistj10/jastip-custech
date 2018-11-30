(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeleteController',OfferingDeleteController);

    OfferingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Offering'];

    function OfferingDeleteController($uibModalInstance, entity, Offering) {
        var vm = this;

        vm.offering = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Offering.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
