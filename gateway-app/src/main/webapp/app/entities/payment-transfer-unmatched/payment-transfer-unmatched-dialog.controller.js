(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferUnmatchedDialogController', PaymentTransferUnmatchedDialogController);

    PaymentTransferUnmatchedDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentTransferUnmatched'];

    function PaymentTransferUnmatchedDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentTransferUnmatched) {
        var vm = this;

        vm.paymentTransferUnmatched = entity;
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
            if (vm.paymentTransferUnmatched.id !== null) {
                PaymentTransferUnmatched.update(vm.paymentTransferUnmatched, onSaveSuccess, onSaveError);
            } else {
                PaymentTransferUnmatched.save(vm.paymentTransferUnmatched, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:paymentTransferUnmatchedUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.paymentUnmatchedDateTime = false;
        vm.datePickerOpenStatus.checkDateTime = false;
        vm.datePickerOpenStatus.expiredDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
