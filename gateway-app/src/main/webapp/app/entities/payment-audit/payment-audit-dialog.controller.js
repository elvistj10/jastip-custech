(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditDialogController', PaymentAuditDialogController);

    PaymentAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentAudit'];

    function PaymentAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentAudit) {
        var vm = this;

        vm.paymentAudit = entity;
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
            if (vm.paymentAudit.id !== null) {
                PaymentAudit.update(vm.paymentAudit, onSaveSuccess, onSaveError);
            } else {
                PaymentAudit.save(vm.paymentAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:paymentAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
