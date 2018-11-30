(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BusinessAccountDeleteController',BusinessAccountDeleteController);

    BusinessAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'BusinessAccount'];

    function BusinessAccountDeleteController($uibModalInstance, entity, BusinessAccount) {
        var vm = this;

        vm.businessAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BusinessAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
