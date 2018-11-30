(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletTransactionDialogController', WalletTransactionDialogController);

    WalletTransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WalletTransaction', 'Wallet'];

    function WalletTransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WalletTransaction, Wallet) {
        var vm = this;

        vm.walletTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wallets = Wallet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.walletTransaction.id !== null) {
                WalletTransaction.update(vm.walletTransaction, onSaveSuccess, onSaveError);
            } else {
                WalletTransaction.save(vm.walletTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:walletTransactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.transactionDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
