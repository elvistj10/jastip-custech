(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCountryDetailController', MCountryDetailController);

    MCountryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MCountry'];

    function MCountryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MCountry) {
        var vm = this;

        vm.mCountry = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:mCountryUpdate', function(event, result) {
            vm.mCountry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
