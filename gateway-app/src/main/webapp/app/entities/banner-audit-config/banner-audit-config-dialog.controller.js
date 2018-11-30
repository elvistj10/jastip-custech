(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditConfigDialogController', BannerAuditConfigDialogController);

    BannerAuditConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BannerAuditConfig'];

    function BannerAuditConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BannerAuditConfig) {
        var vm = this;

        vm.bannerAuditConfig = entity;
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
            if (vm.bannerAuditConfig.id !== null) {
                BannerAuditConfig.update(vm.bannerAuditConfig, onSaveSuccess, onSaveError);
            } else {
                BannerAuditConfig.save(vm.bannerAuditConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:bannerAuditConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
