(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MSubCategoryDialogController', MSubCategoryDialogController);

    MSubCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MSubCategory', 'MCategory'];

    function MSubCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MSubCategory, MCategory) {
        var vm = this;

        vm.mSubCategory = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.mcategories = MCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mSubCategory.id !== null) {
                MSubCategory.update(vm.mSubCategory, onSaveSuccess, onSaveError);
            } else {
                MSubCategory.save(vm.mSubCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mSubCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setSubCategoryIcon = function ($file, mSubCategory) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mSubCategory.subCategoryIcon = base64Data;
                        mSubCategory.subCategoryIconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
