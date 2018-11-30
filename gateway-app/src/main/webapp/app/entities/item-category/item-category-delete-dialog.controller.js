(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemCategoryDeleteController',ItemCategoryDeleteController);

    ItemCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ItemCategory'];

    function ItemCategoryDeleteController($uibModalInstance, entity, ItemCategory) {
        var vm = this;

        vm.itemCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ItemCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
