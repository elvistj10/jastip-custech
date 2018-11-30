(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('UpdatesDialogController', UpdatesDialogController);

    UpdatesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Updates'];

    function UpdatesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Updates) {
        var vm = this;

        vm.updates = entity;
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
            if (vm.updates.id !== null) {
                Updates.update(vm.updates, onSaveSuccess, onSaveError);
            } else {
                Updates.save(vm.updates, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:updatesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.updateDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
