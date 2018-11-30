(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowerListDeleteController',FollowerListDeleteController);

    FollowerListDeleteController.$inject = ['$uibModalInstance', 'entity', 'FollowerList'];

    function FollowerListDeleteController($uibModalInstance, entity, FollowerList) {
        var vm = this;

        vm.followerList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FollowerList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
