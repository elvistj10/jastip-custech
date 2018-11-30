(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('SubCommentDeleteController',SubCommentDeleteController);

    SubCommentDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubComment'];

    function SubCommentDeleteController($uibModalInstance, entity, SubComment) {
        var vm = this;

        vm.subComment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SubComment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
