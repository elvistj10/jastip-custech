(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPuchaseDeleteController',OfferingPuchaseDeleteController);

    OfferingPuchaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferingPuchase'];

    function OfferingPuchaseDeleteController($uibModalInstance, entity, OfferingPuchase) {
        var vm = this;

        vm.offeringPuchase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferingPuchase.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
