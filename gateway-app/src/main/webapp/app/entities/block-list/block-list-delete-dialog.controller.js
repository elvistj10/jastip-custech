(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockListDeleteController',BlockListDeleteController);

    BlockListDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlockList'];

    function BlockListDeleteController($uibModalInstance, entity, BlockList) {
        var vm = this;

        vm.blockList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlockList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
