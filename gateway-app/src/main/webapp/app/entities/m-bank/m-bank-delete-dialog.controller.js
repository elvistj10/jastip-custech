(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MBankDeleteController',MBankDeleteController);

    MBankDeleteController.$inject = ['$uibModalInstance', 'entity', 'MBank'];

    function MBankDeleteController($uibModalInstance, entity, MBank) {
        var vm = this;

        vm.mBank = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MBank.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
