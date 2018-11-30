(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PackageSizeDialogController', PackageSizeDialogController);

    PackageSizeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'PackageSize'];

    function PackageSizeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, PackageSize) {
        var vm = this;

        vm.packageSize = entity;
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
            if (vm.packageSize.id !== null) {
                PackageSize.update(vm.packageSize, onSaveSuccess, onSaveError);
            } else {
                PackageSize.save(vm.packageSize, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:packageSizeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPackageSizeIcon = function ($file, packageSize) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        packageSize.packageSizeIcon = base64Data;
                        packageSize.packageSizeIconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
