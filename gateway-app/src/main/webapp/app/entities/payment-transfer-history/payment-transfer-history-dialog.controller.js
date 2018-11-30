(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferHistoryDialogController', PaymentTransferHistoryDialogController);

    PaymentTransferHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentTransferHistory'];

    function PaymentTransferHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentTransferHistory) {
        var vm = this;

        vm.paymentTransferHistory = entity;
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
            if (vm.paymentTransferHistory.id !== null) {
                PaymentTransferHistory.update(vm.paymentTransferHistory, onSaveSuccess, onSaveError);
            } else {
                PaymentTransferHistory.save(vm.paymentTransferHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:paymentTransferHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.paymentConfirmDateTime = false;
        vm.datePickerOpenStatus.checkDateTime = false;
        vm.datePickerOpenStatus.expiredDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
