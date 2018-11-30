(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditConfigDialogController', ProfilesAuditConfigDialogController);

    ProfilesAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProfilesAuditConfig'];

    function ProfilesAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProfilesAuditConfig) {
        var vm = this;

        vm.profilesAuditConfig = entity;
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
            if (vm.profilesAuditConfig.id !== null) {
                ProfilesAuditConfig.update(vm.profilesAuditConfig, onSaveSuccess, onSaveError);
            } else {
                ProfilesAuditConfig.save(vm.profilesAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:profilesAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
