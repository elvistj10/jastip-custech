(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditDetailController', ProfilesAuditDetailController);

    ProfilesAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProfilesAudit'];

    function ProfilesAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, ProfilesAudit) {
        var vm = this;

        vm.profilesAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:profilesAuditUpdate', function(event, result) {
            vm.profilesAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
