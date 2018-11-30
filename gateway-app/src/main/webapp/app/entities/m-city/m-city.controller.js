(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCityController', MCityController);

    MCityController.$inject = ['MCity'];

    function MCityController(MCity) {

        var vm = this;

        vm.mCities = [];

        loadAll();

        function loadAll() {
            MCity.query(function(result) {
                vm.mCities = result;
                vm.searchQuery = null;
            });
        }
    }
})();
