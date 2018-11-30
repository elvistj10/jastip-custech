(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingCourierDeleteController',OfferingCourierDeleteController);

    OfferingCourierDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferingCourier'];

    function OfferingCourierDeleteController($uibModalInstance, entity, OfferingCourier) {
        var vm = this;

        vm.offeringCourier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferingCourier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
