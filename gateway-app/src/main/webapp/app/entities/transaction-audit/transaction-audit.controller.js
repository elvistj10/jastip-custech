(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditController', TransactionAuditController);

    TransactionAuditController.$inject = ['TransactionAudit'];

    function TransactionAuditController(TransactionAudit) {

        var vm = this;

        vm.transactionAudits = [];

        loadAll();

        function loadAll() {
            TransactionAudit.query(function(result) {
                vm.transactionAudits = result;
                vm.searchQuery = null;
            });
        }
    }
})();
