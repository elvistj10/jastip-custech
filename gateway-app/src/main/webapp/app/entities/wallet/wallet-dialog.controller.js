(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletDialogController', WalletDialogController);

    WalletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wallet', 'WalletTransaction'];

    function WalletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Wallet, WalletTransaction) {
        var vm = this;

        vm.wallet = entity;
        vm.clear = clear;
        vm.save = save;
        vm.wallettransactions = WalletTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wallet.id !== null) {
                Wallet.update(vm.wallet, onSaveSuccess, onSaveError);
            } else {
                Wallet.save(vm.wallet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:walletUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
