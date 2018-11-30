(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowerListDetailController', FollowerListDetailController);

    FollowerListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FollowerList', 'Profile'];

    function FollowerListDetailController($scope, $rootScope, $stateParams, previousState, entity, FollowerList, Profile) {
        var vm = this;

        vm.followerList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:followerListUpdate', function(event, result) {
            vm.followerList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
