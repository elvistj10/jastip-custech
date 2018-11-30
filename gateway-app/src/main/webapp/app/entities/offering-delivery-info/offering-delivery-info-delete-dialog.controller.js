(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryInfoDeleteController',OfferingDeliveryInfoDeleteController);

    OfferingDeliveryInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferingDeliveryInfo'];

    function OfferingDeliveryInfoDeleteController($uibModalInstance, entity, OfferingDeliveryInfo) {
        var vm = this;

        vm.offeringDeliveryInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferingDeliveryInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
