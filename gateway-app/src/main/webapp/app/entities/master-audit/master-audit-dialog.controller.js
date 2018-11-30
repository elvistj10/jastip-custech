(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditDialogController', MasterAuditDialogController);

    MasterAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MasterAudit'];

    function MasterAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MasterAudit) {
        var vm = this;

        vm.masterAudit = entity;
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
            if (vm.masterAudit.id !== null) {
                MasterAudit.update(vm.masterAudit, onSaveSuccess, onSaveError);
            } else {
                MasterAudit.save(vm.masterAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:masterAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
