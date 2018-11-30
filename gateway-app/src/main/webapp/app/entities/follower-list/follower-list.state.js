(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('follower-list', {
            parent: 'entity',
            url: '/follower-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowerLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/follower-list/follower-lists.html',
                    controller: 'FollowerListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('follower-list-detail', {
            parent: 'follower-list',
            url: '/follower-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FollowerList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/follower-list/follower-list-detail.html',
                    controller: 'FollowerListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FollowerList', function($stateParams, FollowerList) {
                    return FollowerList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'follower-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('follower-list-detail.edit', {
            parent: 'follower-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follower-list/follower-list-dialog.html',
                    controller: 'FollowerListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FollowerList', function(FollowerList) {
                            return FollowerList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('follower-list.new', {
            parent: 'follower-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follower-list/follower-list-dialog.html',
                    controller: 'FollowerListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                followerProfileId: null,
                                followedDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('follower-list', null, { reload: 'follower-list' });
                }, function() {
                    $state.go('follower-list');
                });
            }]
        })
        .state('follower-list.edit', {
            parent: 'follower-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follower-list/follower-list-dialog.html',
                    controller: 'FollowerListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FollowerList', function(FollowerList) {
                            return FollowerList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('follower-list', null, { reload: 'follower-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('follower-list.delete', {
            parent: 'follower-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/follower-list/follower-list-delete-dialog.html',
                    controller: 'FollowerListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FollowerList', function(FollowerList) {
                            return FollowerList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('follower-list', null, { reload: 'follower-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
