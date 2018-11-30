(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowingListDeleteController',FollowingListDeleteController);

    FollowingListDeleteController.$inject = ['$uibModalInstance', 'entity', 'FollowingList'];

    function FollowingListDeleteController($uibModalInstance, entity, FollowingList) {
        var vm = this;

        vm.followingList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FollowingList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
