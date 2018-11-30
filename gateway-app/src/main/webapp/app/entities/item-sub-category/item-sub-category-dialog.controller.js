(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemSubCategoryDialogController', ItemSubCategoryDialogController);

    ItemSubCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ItemSubCategory', 'ItemCategory'];

    function ItemSubCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ItemSubCategory, ItemCategory) {
        var vm = this;

        vm.itemSubCategory = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.itemcategories = ItemCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itemSubCategory.id !== null) {
                ItemSubCategory.update(vm.itemSubCategory, onSaveSuccess, onSaveError);
            } else {
                ItemSubCategory.save(vm.itemSubCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:itemSubCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setItemSubCategoryIcon = function ($file, itemSubCategory) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        itemSubCategory.itemSubCategoryIcon = base64Data;
                        itemSubCategory.itemSubCategoryIconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
