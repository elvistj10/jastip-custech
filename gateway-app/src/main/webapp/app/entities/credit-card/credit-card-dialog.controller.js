(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CreditCardDialogController', CreditCardDialogController);

    CreditCardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CreditCard', 'Profile'];

    function CreditCardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CreditCard, Profile) {
        var vm = this;

        vm.creditCard = entity;
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
            if (vm.creditCard.id !== null) {
                CreditCard.update(vm.creditCard, onSaveSuccess, onSaveError);
            } else {
                CreditCard.save(vm.creditCard, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:creditCardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expireMon = false;
        vm.datePickerOpenStatus.expireYear = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
