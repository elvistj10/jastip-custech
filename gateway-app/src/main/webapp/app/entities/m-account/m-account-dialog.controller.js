(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MAccountDialogController', MAccountDialogController);

    MAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MAccount'];

    function MAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MAccount) {
        var vm = this;

        vm.mAccount = entity;
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
            if (vm.mAccount.id !== null) {
                MAccount.update(vm.mAccount, onSaveSuccess, onSaveError);
            } else {
                MAccount.save(vm.mAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
