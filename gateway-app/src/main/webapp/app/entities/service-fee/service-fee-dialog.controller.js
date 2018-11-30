(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ServiceFeeDialogController', ServiceFeeDialogController);

    ServiceFeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceFee'];

    function ServiceFeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceFee) {
        var vm = this;

        vm.serviceFee = entity;
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
            if (vm.serviceFee.id !== null) {
                ServiceFee.update(vm.serviceFee, onSaveSuccess, onSaveError);
            } else {
                ServiceFee.save(vm.serviceFee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:serviceFeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDateTime = false;
        vm.datePickerOpenStatus.endDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
