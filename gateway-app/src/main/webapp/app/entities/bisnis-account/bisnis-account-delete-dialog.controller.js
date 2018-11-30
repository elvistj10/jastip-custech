(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BisnisAccountDeleteController',BisnisAccountDeleteController);

    BisnisAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'BisnisAccount'];

    function BisnisAccountDeleteController($uibModalInstance, entity, BisnisAccount) {
        var vm = this;

        vm.bisnisAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BisnisAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
