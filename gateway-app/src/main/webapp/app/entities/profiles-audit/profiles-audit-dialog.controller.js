(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditDialogController', ProfilesAuditDialogController);

    ProfilesAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProfilesAudit'];

    function ProfilesAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProfilesAudit) {
        var vm = this;

        vm.profilesAudit = entity;
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
            if (vm.profilesAudit.id !== null) {
                ProfilesAudit.update(vm.profilesAudit, onSaveSuccess, onSaveError);
            } else {
                ProfilesAudit.save(vm.profilesAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:profilesAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
