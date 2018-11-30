(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerAuditConfigDetailController', BannerAuditConfigDetailController);

    BannerAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BannerAuditConfig'];

    function BannerAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, BannerAuditConfig) {
        var vm = this;

        vm.bannerAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:bannerAuditConfigUpdate', function(event, result) {
            vm.bannerAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
