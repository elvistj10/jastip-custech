(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MSubCategoryDeleteController',MSubCategoryDeleteController);

    MSubCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'MSubCategory'];

    function MSubCategoryDeleteController($uibModalInstance, entity, MSubCategory) {
        var vm = this;

        vm.mSubCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MSubCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
