(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ItemCategoryDetailController', ItemCategoryDetailController);

    ItemCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ItemCategory', 'ItemSubCategory'];

    function ItemCategoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ItemCategory, ItemSubCategory) {
        var vm = this;

        vm.itemCategory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:itemCategoryUpdate', function(event, result) {
            vm.itemCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
