(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MPackageSizeController', MPackageSizeController);

    MPackageSizeController.$inject = ['DataUtils', 'MPackageSize'];

    function MPackageSizeController(DataUtils, MPackageSize) {

        var vm = this;

        vm.mPackageSizes = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            MPackageSize.query(function(result) {
                vm.mPackageSizes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
