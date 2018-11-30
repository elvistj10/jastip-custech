(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('block-list', {
            parent: 'entity',
            url: '/block-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BlockLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/block-list/block-lists.html',
                    controller: 'BlockListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('block-list-detail', {
            parent: 'block-list',
            url: '/block-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BlockList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/block-list/block-list-detail.html',
                    controller: 'BlockListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BlockList', function($stateParams, BlockList) {
                    return BlockList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'block-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('block-list-detail.edit', {
            parent: 'block-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-list/block-list-dialog.html',
                    controller: 'BlockListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockList', function(BlockList) {
                            return BlockList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('block-list.new', {
            parent: 'block-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-list/block-list-dialog.html',
                    controller: 'BlockListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                blockedProfileId: null,
                                blockDate: null,
                                block: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('block-list', null, { reload: 'block-list' });
                }, function() {
                    $state.go('block-list');
                });
            }]
        })
        .state('block-list.edit', {
            parent: 'block-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-list/block-list-dialog.html',
                    controller: 'BlockListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlockList', function(BlockList) {
                            return BlockList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('block-list', null, { reload: 'block-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('block-list.delete', {
            parent: 'block-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/block-list/block-list-delete-dialog.html',
                    controller: 'BlockListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BlockList', function(BlockList) {
                            return BlockList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('block-list', null, { reload: 'block-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
