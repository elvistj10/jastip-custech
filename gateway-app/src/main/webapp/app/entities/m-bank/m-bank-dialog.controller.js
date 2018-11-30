(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MBankDialogController', MBankDialogController);

    MBankDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MBank'];

    function MBankDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MBank) {
        var vm = this;

        vm.mBank = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mBank.id !== null) {
                MBank.update(vm.mBank, onSaveSuccess, onSaveError);
            } else {
                MBank.save(vm.mBank, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mBankUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
