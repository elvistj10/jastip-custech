(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferListDialogController', WithdrawalTransferListDialogController);

    WithdrawalTransferListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WithdrawalTransferList'];

    function WithdrawalTransferListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WithdrawalTransferList) {
        var vm = this;

        vm.withdrawalTransferList = entity;
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
            if (vm.withdrawalTransferList.id !== null) {
                WithdrawalTransferList.update(vm.withdrawalTransferList, onSaveSuccess, onSaveError);
            } else {
                WithdrawalTransferList.save(vm.withdrawalTransferList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:withdrawalTransferListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
