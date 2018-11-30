(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditConfigDialogController', MasterAuditConfigDialogController);

    MasterAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MasterAuditConfig'];

    function MasterAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MasterAuditConfig) {
        var vm = this;

        vm.masterAuditConfig = entity;
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
            if (vm.masterAuditConfig.id !== null) {
                MasterAuditConfig.update(vm.masterAuditConfig, onSaveSuccess, onSaveError);
            } else {
                MasterAuditConfig.save(vm.masterAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:masterAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
