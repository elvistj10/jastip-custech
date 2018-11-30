(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditConfigDetailController', ProfilesAuditConfigDetailController);

    ProfilesAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProfilesAuditConfig'];

    function ProfilesAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, ProfilesAuditConfig) {
        var vm = this;

        vm.profilesAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:profilesAuditConfigUpdate', function(event, result) {
            vm.profilesAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
