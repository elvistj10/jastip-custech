(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostalCodeDialogController', PostalCodeDialogController);

    PostalCodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PostalCode', 'City'];

    function PostalCodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PostalCode, City) {
        var vm = this;

        vm.postalCode = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.postalCode.id !== null) {
                PostalCode.update(vm.postalCode, onSaveSuccess, onSaveError);
            } else {
                PostalCode.save(vm.postalCode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:postalCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
