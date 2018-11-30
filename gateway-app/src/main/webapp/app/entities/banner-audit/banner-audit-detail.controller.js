(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditDetailController', BannerAuditDetailController);

    BannerAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BannerAudit'];

    function BannerAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, BannerAudit) {
        var vm = this;

        vm.bannerAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:bannerAuditUpdate', function(event, result) {
            vm.bannerAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
