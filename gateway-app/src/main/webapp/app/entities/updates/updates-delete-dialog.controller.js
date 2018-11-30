(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('UpdatesDeleteController',UpdatesDeleteController);

    UpdatesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Updates'];

    function UpdatesDeleteController($uibModalInstance, entity, Updates) {
        var vm = this;

        vm.updates = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Updates.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
