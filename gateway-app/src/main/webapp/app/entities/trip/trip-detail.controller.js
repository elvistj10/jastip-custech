(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TripDetailController', TripDetailController);

    TripDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trip', 'Posting'];

    function TripDetailController($scope, $rootScope, $stateParams, previousState, entity, Trip, Posting) {
        var vm = this;

        vm.trip = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:tripUpdate', function(event, result) {
            vm.trip = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
