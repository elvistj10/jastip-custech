(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BisnisAccountDetailController', BisnisAccountDetailController);

    BisnisAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BisnisAccount'];

    function BisnisAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, BisnisAccount) {
        var vm = this;

        vm.bisnisAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:bisnisAccountUpdate', function(event, result) {
            vm.bisnisAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
