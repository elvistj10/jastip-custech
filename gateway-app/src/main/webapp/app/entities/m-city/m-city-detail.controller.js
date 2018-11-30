(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCityDetailController', MCityDetailController);

    MCityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MCity', 'MProvince'];

    function MCityDetailController($scope, $rootScope, $stateParams, previousState, entity, MCity, MProvince) {
        var vm = this;

        vm.mCity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:mCityUpdate', function(event, result) {
            vm.mCity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
