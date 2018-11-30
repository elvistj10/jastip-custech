(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MBankController', MBankController);

    MBankController.$inject = ['MBank'];

    function MBankController(MBank) {

        var vm = this;

        vm.mBanks = [];

        loadAll();

        function loadAll() {
            MBank.query(function(result) {
                vm.mBanks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
