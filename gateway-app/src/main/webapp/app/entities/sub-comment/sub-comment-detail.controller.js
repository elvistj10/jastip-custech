(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('SubCommentDetailController', SubCommentDetailController);

    SubCommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubComment', 'Comment'];

    function SubCommentDetailController($scope, $rootScope, $stateParams, previousState, entity, SubComment, Comment) {
        var vm = this;

        vm.subComment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:subCommentUpdate', function(event, result) {
            vm.subComment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
