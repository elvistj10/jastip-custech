(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentTransferCheckListDialogController', PaymentTransferCheckListDialogController);

    PaymentTransferCheckListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentTransferCheckList'];

    function PaymentTransferCheckListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentTransferCheckList) {
        var vm = this;

        vm.paymentTransferCheckList = entity;
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
            if (vm.paymentTransferCheckList.id !== null) {
                PaymentTransferCheckList.update(vm.paymentTransferCheckList, onSaveSuccess, onSaveError);
            } else {
                PaymentTransferCheckList.save(vm.paymentTransferCheckList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:paymentTransferCheckListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.paymentConfirmDateTime = false;
        vm.datePickerOpenStatus.expiredDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
