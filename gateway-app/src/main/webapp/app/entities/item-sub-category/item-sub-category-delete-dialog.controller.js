(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemSubCategoryDeleteController',ItemSubCategoryDeleteController);

    ItemSubCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ItemSubCategory'];

    function ItemSubCategoryDeleteController($uibModalInstance, entity, ItemSubCategory) {
        var vm = this;

        vm.itemSubCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ItemSubCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
