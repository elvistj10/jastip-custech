(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MSubCategoryController', MSubCategoryController);

    MSubCategoryController.$inject = ['DataUtils', 'MSubCategory'];

    function MSubCategoryController(DataUtils, MSubCategory) {

        var vm = this;

        vm.mSubCategories = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            MSubCategory.query(function(result) {
                vm.mSubCategories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
