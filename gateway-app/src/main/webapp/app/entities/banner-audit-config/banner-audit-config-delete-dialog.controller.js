(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditConfigDeleteController',BannerAuditConfigDeleteController);

    BannerAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'BannerAuditConfig'];

    function BannerAuditConfigDeleteController($uibModalInstance, entity, BannerAuditConfig) {
        var vm = this;

        vm.bannerAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BannerAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
