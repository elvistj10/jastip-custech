(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditConfigDialogController', TransactionAuditConfigDialogController);

    TransactionAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransactionAuditConfig'];

    function TransactionAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransactionAuditConfig) {
        var vm = this;

        vm.transactionAuditConfig = entity;
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
            if (vm.transactionAuditConfig.id !== null) {
                TransactionAuditConfig.update(vm.transactionAuditConfig, onSaveSuccess, onSaveError);
            } else {
                TransactionAuditConfig.save(vm.transactionAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:transactionAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
