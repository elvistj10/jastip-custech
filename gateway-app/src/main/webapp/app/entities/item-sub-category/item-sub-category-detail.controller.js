(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemSubCategoryDetailController', ItemSubCategoryDetailController);

    ItemSubCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ItemSubCategory', 'ItemCategory'];

    function ItemSubCategoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ItemSubCategory, ItemCategory) {
        var vm = this;

        vm.itemSubCategory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:itemSubCategoryUpdate', function(event, result) {
            vm.itemSubCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
