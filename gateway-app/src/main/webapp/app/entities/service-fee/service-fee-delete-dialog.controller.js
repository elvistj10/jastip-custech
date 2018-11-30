(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ServiceFeeDeleteController',ServiceFeeDeleteController);

    ServiceFeeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceFee'];

    function ServiceFeeDeleteController($uibModalInstance, entity, ServiceFee) {
        var vm = this;

        vm.serviceFee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceFee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
