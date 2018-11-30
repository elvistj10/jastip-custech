(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('UpdatesDetailController', UpdatesDetailController);

    UpdatesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Updates'];

    function UpdatesDetailController($scope, $rootScope, $stateParams, previousState, entity, Updates) {
        var vm = this;

        vm.updates = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:updatesUpdate', function(event, result) {
            vm.updates = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
