(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MAccountDeleteController',MAccountDeleteController);

    MAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'MAccount'];

    function MAccountDeleteController($uibModalInstance, entity, MAccount) {
        var vm = this;

        vm.mAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
