(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCountryDialogController', MCountryDialogController);

    MCountryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MCountry'];

    function MCountryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MCountry) {
        var vm = this;

        vm.mCountry = entity;
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
            if (vm.mCountry.id !== null) {
                MCountry.update(vm.mCountry, onSaveSuccess, onSaveError);
            } else {
                MCountry.save(vm.mCountry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mCountryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, mCountry) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mCountry.image = base64Data;
                        mCountry.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.setImageThumbUrl = function ($file, mCountry) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mCountry.imageThumbUrl = base64Data;
                        mCountry.imageThumbUrlContentType = $file.type;
                    });
                });
            }
        };

        vm.setImageFlag = function ($file, mCountry) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        mCountry.imageFlag = base64Data;
                        mCountry.imageFlagContentType = $file.type;
                    });
                });
            }
        };

    }
})();
