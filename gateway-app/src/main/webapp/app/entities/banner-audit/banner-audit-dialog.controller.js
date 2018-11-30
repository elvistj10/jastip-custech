(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditDialogController', BannerAuditDialogController);

    BannerAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BannerAudit'];

    function BannerAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BannerAudit) {
        var vm = this;

        vm.bannerAudit = entity;
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
            if (vm.bannerAudit.id !== null) {
                BannerAudit.update(vm.bannerAudit, onSaveSuccess, onSaveError);
            } else {
                BannerAudit.save(vm.bannerAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:bannerAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
