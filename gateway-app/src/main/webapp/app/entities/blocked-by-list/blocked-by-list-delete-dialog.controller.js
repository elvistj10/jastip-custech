(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockedByListDeleteController',BlockedByListDeleteController);

    BlockedByListDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlockedByList'];

    function BlockedByListDeleteController($uibModalInstance, entity, BlockedByList) {
        var vm = this;

        vm.blockedByList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlockedByList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
