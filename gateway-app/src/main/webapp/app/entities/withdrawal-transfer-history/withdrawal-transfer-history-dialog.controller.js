(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WithdrawalTransferHistoryDialogController', WithdrawalTransferHistoryDialogController);

    WithdrawalTransferHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WithdrawalTransferHistory'];

    function WithdrawalTransferHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WithdrawalTransferHistory) {
        var vm = this;

        vm.withdrawalTransferHistory = entity;
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
            if (vm.withdrawalTransferHistory.id !== null) {
                WithdrawalTransferHistory.update(vm.withdrawalTransferHistory, onSaveSuccess, onSaveError);
            } else {
                WithdrawalTransferHistory.save(vm.withdrawalTransferHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:withdrawalTransferHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
