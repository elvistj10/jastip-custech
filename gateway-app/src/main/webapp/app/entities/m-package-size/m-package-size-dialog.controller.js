(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MPackageSizeDialogController', MPackageSizeDialogController);

    MPackageSizeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MPackageSize'];

    function MPackageSizeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MPackageSize) {
        var vm = this;

        vm.mPackageSize = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mPackageSize.id !== null) {
                MPackageSize.update(vm.mPackageSize, onSaveSuccess, onSaveError);
            } else {
                MPackageSize.save(vm.mPackageSize, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mPackageSizeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPackageSizeThumb = function ($file, mPackageSize) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mPackageSize.packageSizeThumb = base64Data;
                        mPackageSize.packageSizeThumbContentType = $file.type;
                    });
                });
            }
        };

    }
})();
