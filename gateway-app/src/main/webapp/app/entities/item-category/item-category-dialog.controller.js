(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemCategoryDialogController', ItemCategoryDialogController);

    ItemCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ItemCategory', 'ItemSubCategory'];

    function ItemCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ItemCategory, ItemSubCategory) {
        var vm = this;

        vm.itemCategory = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.itemsubcategories = ItemSubCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itemCategory.id !== null) {
                ItemCategory.update(vm.itemCategory, onSaveSuccess, onSaveError);
            } else {
                ItemCategory.save(vm.itemCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:itemCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setItemCategoryIcon = function ($file, itemCategory) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        itemCategory.itemCategoryIcon = base64Data;
                        itemCategory.itemCategoryIconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
