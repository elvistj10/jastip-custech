(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MAccountController', MAccountController);

    MAccountController.$inject = ['MAccount'];

    function MAccountController(MAccount) {

        var vm = this;

        vm.mAccounts = [];

        loadAll();

        function loadAll() {
            MAccount.query(function(result) {
                vm.mAccounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
