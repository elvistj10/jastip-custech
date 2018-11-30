(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostalCodeDeleteController',PostalCodeDeleteController);

    PostalCodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PostalCode'];

    function PostalCodeDeleteController($uibModalInstance, entity, PostalCode) {
        var vm = this;

        vm.postalCode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PostalCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
