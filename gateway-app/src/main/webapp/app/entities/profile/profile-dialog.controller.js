(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfileDialogController', ProfileDialogController);

    ProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Profile', 'Address', 'BankAccount', 'CreditCard', 'FollowingList', 'FollowerList', 'BlockList', 'BlockedByList', 'Feedback'];

    function ProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Profile, Address, BankAccount, CreditCard, FollowingList, FollowerList, BlockList, BlockedByList, Feedback) {
        var vm = this;

        vm.profile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.addresses = Address.query();
        vm.bankaccounts = BankAccount.query();
        vm.creditcards = CreditCard.query();
        vm.followinglists = FollowingList.query();
        vm.followerlists = FollowerList.query();
        vm.blocklists = BlockList.query();
        vm.blockedbylists = BlockedByList.query();
        vm.feedbacks = Feedback.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.profile.id !== null) {
                Profile.update(vm.profile, onSaveSuccess, onSaveError);
            } else {
                Profile.save(vm.profile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:profileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.activeDate = false;

        vm.setImage = function ($file, profile) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        profile.image = base64Data;
                        profile.imageContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
