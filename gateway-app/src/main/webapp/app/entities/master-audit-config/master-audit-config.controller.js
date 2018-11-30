(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditConfigController', MasterAuditConfigController);

    MasterAuditConfigController.$inject = ['MasterAuditConfig'];

    function MasterAuditConfigController(MasterAuditConfig) {

        var vm = this;

        vm.masterAuditConfigs = [];

        loadAll();

        function loadAll() {
            MasterAuditConfig.query(function(result) {
                vm.masterAuditConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
