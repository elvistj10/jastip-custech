(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BusinessAccountDialogController', BusinessAccountDialogController);

    BusinessAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BusinessAccount'];

    function BusinessAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BusinessAccount) {
        var vm = this;

        vm.businessAccount = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.businessAccount.id !== null) {
                BusinessAccount.update(vm.businessAccount, onSaveSuccess, onSaveError);
            } else {
                BusinessAccount.save(vm.businessAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:businessAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
