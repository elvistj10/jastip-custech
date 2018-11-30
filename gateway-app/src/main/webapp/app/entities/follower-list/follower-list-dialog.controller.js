(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowerListDialogController', FollowerListDialogController);

    FollowerListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FollowerList', 'Profile'];

    function FollowerListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FollowerList, Profile) {
        var vm = this;

        vm.followerList = entity;
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
            if (vm.followerList.id !== null) {
                FollowerList.update(vm.followerList, onSaveSuccess, onSaveError);
            } else {
                FollowerList.save(vm.followerList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:followerListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.followedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
