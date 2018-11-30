(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sub-comment', {
            parent: 'entity',
            url: '/sub-comment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SubComments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sub-comment/sub-comments.html',
                    controller: 'SubCommentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('sub-comment-detail', {
            parent: 'sub-comment',
            url: '/sub-comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SubComment'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sub-comment/sub-comment-detail.html',
                    controller: 'SubCommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SubComment', function($stateParams, SubComment) {
                    return SubComment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sub-comment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sub-comment-detail.edit', {
            parent: 'sub-comment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-comment/sub-comment-dialog.html',
                    controller: 'SubCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubComment', function(SubComment) {
                            return SubComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sub-comment.new', {
            parent: 'sub-comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-comment/sub-comment-dialog.html',
                    controller: 'SubCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                profileId: null,
                                subComment: null,
                                subCommentDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sub-comment', null, { reload: 'sub-comment' });
                }, function() {
                    $state.go('sub-comment');
                });
            }]
        })
        .state('sub-comment.edit', {
            parent: 'sub-comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-comment/sub-comment-dialog.html',
                    controller: 'SubCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubComment', function(SubComment) {
                            return SubComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sub-comment', null, { reload: 'sub-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sub-comment.delete', {
            parent: 'sub-comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-comment/sub-comment-delete-dialog.html',
                    controller: 'SubCommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubComment', function(SubComment) {
                            return SubComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sub-comment', null, { reload: 'sub-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
