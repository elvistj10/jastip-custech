(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockedByListDialogController', BlockedByListDialogController);

    BlockedByListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlockedByList', 'Profile'];

    function BlockedByListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BlockedByList, Profile) {
        var vm = this;

        vm.blockedByList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.profiles = Profile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blockedByList.id !== null) {
                BlockedByList.update(vm.blockedByList, onSaveSuccess, onSaveError);
            } else {
                BlockedByList.save(vm.blockedByList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:blockedByListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.blockDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
