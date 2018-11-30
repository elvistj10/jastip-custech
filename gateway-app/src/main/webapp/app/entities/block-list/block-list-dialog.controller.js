(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockListDialogController', BlockListDialogController);

    BlockListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlockList', 'Profile'];

    function BlockListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BlockList, Profile) {
        var vm = this;

        vm.blockList = entity;
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
            if (vm.blockList.id !== null) {
                BlockList.update(vm.blockList, onSaveSuccess, onSaveError);
            } else {
                BlockList.save(vm.blockList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:blockListUpdate', result);
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
