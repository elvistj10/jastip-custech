(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BisnisAccountDialogController', BisnisAccountDialogController);

    BisnisAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BisnisAccount'];

    function BisnisAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BisnisAccount) {
        var vm = this;

        vm.bisnisAccount = entity;
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
            if (vm.bisnisAccount.id !== null) {
                BisnisAccount.update(vm.bisnisAccount, onSaveSuccess, onSaveError);
            } else {
                BisnisAccount.save(vm.bisnisAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:bisnisAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
