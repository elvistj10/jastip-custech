(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditConfigDialogController', PaymentAuditConfigDialogController);

    PaymentAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentAuditConfig'];

    function PaymentAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentAuditConfig) {
        var vm = this;

        vm.paymentAuditConfig = entity;
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
            if (vm.paymentAuditConfig.id !== null) {
                PaymentAuditConfig.update(vm.paymentAuditConfig, onSaveSuccess, onSaveError);
            } else {
                PaymentAuditConfig.save(vm.paymentAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:paymentAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
