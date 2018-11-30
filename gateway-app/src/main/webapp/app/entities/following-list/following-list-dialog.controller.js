(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowingListDialogController', FollowingListDialogController);

    FollowingListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FollowingList', 'Profile'];

    function FollowingListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FollowingList, Profile) {
        var vm = this;

        vm.followingList = entity;
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
            if (vm.followingList.id !== null) {
                FollowingList.update(vm.followingList, onSaveSuccess, onSaveError);
            } else {
                FollowingList.save(vm.followingList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:followingListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.followingDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
