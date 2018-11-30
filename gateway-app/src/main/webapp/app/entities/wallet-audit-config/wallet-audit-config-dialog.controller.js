(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditConfigDialogController', WalletAuditConfigDialogController);

    WalletAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WalletAuditConfig'];

    function WalletAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WalletAuditConfig) {
        var vm = this;

        vm.walletAuditConfig = entity;
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
            if (vm.walletAuditConfig.id !== null) {
                WalletAuditConfig.update(vm.walletAuditConfig, onSaveSuccess, onSaveError);
            } else {
                WalletAuditConfig.save(vm.walletAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:walletAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
