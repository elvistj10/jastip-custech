(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockedByListDetailController', BlockedByListDetailController);

    BlockedByListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BlockedByList', 'Profile'];

    function BlockedByListDetailController($scope, $rootScope, $stateParams, previousState, entity, BlockedByList, Profile) {
        var vm = this;

        vm.blockedByList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:blockedByListUpdate', function(event, result) {
            vm.blockedByList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
