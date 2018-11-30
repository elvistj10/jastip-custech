(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BisnisAccountController', BisnisAccountController);

    BisnisAccountController.$inject = ['BisnisAccount'];

    function BisnisAccountController(BisnisAccount) {

        var vm = this;

        vm.bisnisAccounts = [];

        loadAll();

        function loadAll() {
            BisnisAccount.query(function(result) {
                vm.bisnisAccounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
