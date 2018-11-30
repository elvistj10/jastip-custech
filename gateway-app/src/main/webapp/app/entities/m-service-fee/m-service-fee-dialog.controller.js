(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MServiceFeeDialogController', MServiceFeeDialogController);

    MServiceFeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MServiceFee'];

    function MServiceFeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MServiceFee) {
        var vm = this;

        vm.mServiceFee = entity;
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
            if (vm.mServiceFee.id !== null) {
                MServiceFee.update(vm.mServiceFee, onSaveSuccess, onSaveError);
            } else {
                MServiceFee.save(vm.mServiceFee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mServiceFeeUpdate', result);
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
