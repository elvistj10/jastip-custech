(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockedByListController', BlockedByListController);

    BlockedByListController.$inject = ['BlockedByList'];

    function BlockedByListController(BlockedByList) {

        var vm = this;

        vm.blockedByLists = [];

        loadAll();

        function loadAll() {
            BlockedByList.query(function(result) {
                vm.blockedByLists = result;
                vm.searchQuery = null;
            });
        }
    }
})();
