(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCategoryDeleteController',MCategoryDeleteController);

    MCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'MCategory'];

    function MCategoryDeleteController($uibModalInstance, entity, MCategory) {
        var vm = this;

        vm.mCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
