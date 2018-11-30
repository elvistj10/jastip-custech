(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCountryController', MCountryController);

    MCountryController.$inject = ['DataUtils', 'MCountry'];

    function MCountryController(DataUtils, MCountry) {

        var vm = this;

        vm.mCountries = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            MCountry.query(function(result) {
                vm.mCountries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
