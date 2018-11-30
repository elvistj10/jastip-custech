(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCategoryDialogController', MCategoryDialogController);

    MCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MCategory', 'MSubCategory'];

    function MCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MCategory, MSubCategory) {
        var vm = this;

        vm.mCategory = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.msubcategories = MSubCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mCategory.id !== null) {
                MCategory.update(vm.mCategory, onSaveSuccess, onSaveError);
            } else {
                MCategory.save(vm.mCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setCategoryIcon = function ($file, mCategory) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mCategory.categoryIcon = base64Data;
                        mCategory.categoryIconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
