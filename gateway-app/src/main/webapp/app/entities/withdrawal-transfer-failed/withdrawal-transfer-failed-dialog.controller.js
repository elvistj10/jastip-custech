(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferFailedDialogController', WithdrawalTransferFailedDialogController);

    WithdrawalTransferFailedDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WithdrawalTransferFailed'];

    function WithdrawalTransferFailedDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WithdrawalTransferFailed) {
        var vm = this;

        vm.withdrawalTransferFailed = entity;
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
            if (vm.withdrawalTransferFailed.id !== null) {
                WithdrawalTransferFailed.update(vm.withdrawalTransferFailed, onSaveSuccess, onSaveError);
            } else {
                WithdrawalTransferFailed.save(vm.withdrawalTransferFailed, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:withdrawalTransferFailedUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
