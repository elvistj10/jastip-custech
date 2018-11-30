(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPaymentDeleteController',OfferingPaymentDeleteController);

    OfferingPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferingPayment'];

    function OfferingPaymentDeleteController($uibModalInstance, entity, OfferingPayment) {
        var vm = this;

        vm.offeringPayment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferingPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
