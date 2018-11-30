(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditDialogController', TransactionAuditDialogController);

    TransactionAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransactionAudit'];

    function TransactionAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransactionAudit) {
        var vm = this;

        vm.transactionAudit = entity;
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
            if (vm.transactionAudit.id !== null) {
                TransactionAudit.update(vm.transactionAudit, onSaveSuccess, onSaveError);
            } else {
                TransactionAudit.save(vm.transactionAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:transactionAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
