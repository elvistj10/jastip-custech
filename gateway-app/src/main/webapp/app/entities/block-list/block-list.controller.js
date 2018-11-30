(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockListController', BlockListController);

    BlockListController.$inject = ['BlockList'];

    function BlockListController(BlockList) {

        var vm = this;

        vm.blockLists = [];

        loadAll();

        function loadAll() {
            BlockList.query(function(result) {
                vm.blockLists = result;
                vm.searchQuery = null;
            });
        }
    }
})();
