(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MProvinceDetailController', MProvinceDetailController);

    MProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MProvince', 'MCountry'];

    function MProvinceDetailController($scope, $rootScope, $stateParams, previousState, entity, MProvince, MCountry) {
        var vm = this;

        vm.mProvince = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:mProvinceUpdate', function(event, result) {
            vm.mProvince = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
