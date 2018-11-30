(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditDeleteController',BannerAuditDeleteController);

    BannerAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'BannerAudit'];

    function BannerAuditDeleteController($uibModalInstance, entity, BannerAudit) {
        var vm = this;

        vm.bannerAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BannerAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
