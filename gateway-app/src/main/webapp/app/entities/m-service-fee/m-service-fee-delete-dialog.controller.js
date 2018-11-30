(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MServiceFeeDeleteController',MServiceFeeDeleteController);

    MServiceFeeDeleteController.$inject = ['$uibModalInstance', 'entity', 'MServiceFee'];

    function MServiceFeeDeleteController($uibModalInstance, entity, MServiceFee) {
        var vm = this;

        vm.mServiceFee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MServiceFee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
