(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MServiceFeeController', MServiceFeeController);

    MServiceFeeController.$inject = ['MServiceFee'];

    function MServiceFeeController(MServiceFee) {

        var vm = this;

        vm.mServiceFees = [];

        loadAll();

        function loadAll() {
            MServiceFee.query(function(result) {
                vm.mServiceFees = result;
                vm.searchQuery = null;
            });
        }
    }
})();
