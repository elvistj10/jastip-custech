(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('blocked-by-list', {
            parent: 'entity',
            url: '/blocked-by-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BlockedByLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-lists.html',
                    controller: 'BlockedByListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('blocked-by-list-detail', {
            parent: 'blocked-by-list',
            url: '/blocked-by-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BlockedByList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-list-detail.html',
                    controller: 'BlockedByListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BlockedByList', function($stateParams, BlockedByList) {
                    return BlockedByList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'blocked-by-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('blocked-by-list-detail.edit', {
            parent: 'blocked-by-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-list-dialog.html',
                    controller: 'BlockedByListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockedByList', function(BlockedByList) {
                            return BlockedByList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('blocked-by-list.new', {
            parent: 'blocked-by-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-list-dialog.html',
                    controller: 'BlockedByListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                blokerProfileId: null,
                                blockDate: null,
                                blocked: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('blocked-by-list', null, { reload: 'blocked-by-list' });
                }, function() {
                    $state.go('blocked-by-list');
                });
            }]
        })
        .state('blocked-by-list.edit', {
            parent: 'blocked-by-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-list-dialog.html',
                    controller: 'BlockedByListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockedByList', function(BlockedByList) {
                            return BlockedByList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('blocked-by-list', null, { reload: 'blocked-by-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('blocked-by-list.delete', {
            parent: 'blocked-by-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blocked-by-list/blocked-by-list-delete-dialog.html',
                    controller: 'BlockedByListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BlockedByList', function(BlockedByList) {
                            return BlockedByList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('blocked-by-list', null, { reload: 'blocked-by-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
