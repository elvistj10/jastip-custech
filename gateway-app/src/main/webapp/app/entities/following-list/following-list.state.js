(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('following-list', {
            parent: 'entity',
            url: '/following-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowingLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/following-list/following-lists.html',
                    controller: 'FollowingListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('following-list-detail', {
            parent: 'following-list',
            url: '/following-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowingList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/following-list/following-list-detail.html',
                    controller: 'FollowingListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FollowingList', function($stateParams, FollowingList) {
                    return FollowingList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'following-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('following-list-detail.edit', {
            parent: 'following-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/following-list/following-list-dialog.html',
                    controller: 'FollowingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FollowingList', function(FollowingList) {
                            return FollowingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('following-list.new', {
            parent: 'following-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/following-list/following-list-dialog.html',
                    controller: 'FollowingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                followingProfileId: null,
                                followingDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('following-list', null, { reload: 'following-list' });
                }, function() {
                    $state.go('following-list');
                });
            }]
        })
        .state('following-list.edit', {
            parent: 'following-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/following-list/following-list-dialog.html',
                    controller: 'FollowingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FollowingList', function(FollowingList) {
                            return FollowingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('following-list', null, { reload: 'following-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('following-list.delete', {
            parent: 'following-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/following-list/following-list-delete-dialog.html',
                    controller: 'FollowingListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FollowingList', function(FollowingList) {
                            return FollowingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('following-list', null, { reload: 'following-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
