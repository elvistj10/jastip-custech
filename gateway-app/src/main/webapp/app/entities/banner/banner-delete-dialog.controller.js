(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerDeleteController',BannerDeleteController);

    BannerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Banner'];

    function BannerDeleteController($uibModalInstance, entity, Banner) {
        var vm = this;

        vm.banner = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Banner.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
