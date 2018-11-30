(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditConfigController', TransactionAuditConfigController);

    TransactionAuditConfigController.$inject = ['TransactionAuditConfig'];

    function TransactionAuditConfigController(TransactionAuditConfig) {

        var vm = this;

        vm.transactionAuditConfigs = [];

        loadAll();

        function loadAll() {
            TransactionAuditConfig.query(function(result) {
                vm.transactionAuditConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
