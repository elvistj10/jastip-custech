(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostingDeleteController',PostingDeleteController);

    PostingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Posting'];

    function PostingDeleteController($uibModalInstance, entity, Posting) {
        var vm = this;

        vm.posting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Posting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
