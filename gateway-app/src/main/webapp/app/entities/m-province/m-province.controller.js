(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MProvinceController', MProvinceController);

    MProvinceController.$inject = ['MProvince'];

    function MProvinceController(MProvince) {

        var vm = this;

        vm.mProvinces = [];

        loadAll();

        function loadAll() {
            MProvince.query(function(result) {
                vm.mProvinces = result;
                vm.searchQuery = null;
            });
        }
    }
})();
