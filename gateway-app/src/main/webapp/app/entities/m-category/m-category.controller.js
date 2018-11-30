(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCategoryController', MCategoryController);

    MCategoryController.$inject = ['DataUtils', 'MCategory'];

    function MCategoryController(DataUtils, MCategory) {

        var vm = this;

        vm.mCategories = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            MCategory.query(function(result) {
                vm.mCategories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
