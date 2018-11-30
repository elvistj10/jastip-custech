(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('WalletAuditDialogController', WalletAuditDialogController);

    WalletAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WalletAudit'];

    function WalletAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WalletAudit) {
        var vm = this;

        vm.walletAudit = entity;
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
            if (vm.walletAudit.id !== null) {
                WalletAudit.update(vm.walletAudit, onSaveSuccess, onSaveError);
            } else {
                WalletAudit.save(vm.walletAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:walletAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
