(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BannerDetailController', BannerDetailController);

    BannerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Banner'];

    function BannerDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Banner) {
        var vm = this;

        vm.banner = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:bannerUpdate', function(event, result) {
            vm.banner = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
