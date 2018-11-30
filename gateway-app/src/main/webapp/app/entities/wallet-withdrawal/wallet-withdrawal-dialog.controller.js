(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletWithdrawalDialogController', WalletWithdrawalDialogController);

    WalletWithdrawalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WalletWithdrawal'];

    function WalletWithdrawalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WalletWithdrawal) {
        var vm = this;

        vm.walletWithdrawal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.walletWithdrawal.id !== null) {
                WalletWithdrawal.update(vm.walletWithdrawal, onSaveSuccess, onSaveError);
            } else {
                WalletWithdrawal.save(vm.walletWithdrawal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:walletWithdrawalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.requestDateTime = false;
        vm.datePickerOpenStatus.completedDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
