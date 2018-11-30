(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CreditCardDeleteController',CreditCardDeleteController);

    CreditCardDeleteController.$inject = ['$uibModalInstance', 'entity', 'CreditCard'];

    function CreditCardDeleteController($uibModalInstance, entity, CreditCard) {
        var vm = this;

        vm.creditCard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CreditCard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
