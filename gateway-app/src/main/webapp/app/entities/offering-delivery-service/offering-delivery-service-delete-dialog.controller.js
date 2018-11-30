(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryServiceDeleteController',OfferingDeliveryServiceDeleteController);

    OfferingDeliveryServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferingDeliveryService'];

    function OfferingDeliveryServiceDeleteController($uibModalInstance, entity, OfferingDeliveryService) {
        var vm = this;

        vm.offeringDeliveryService = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferingDeliveryService.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
