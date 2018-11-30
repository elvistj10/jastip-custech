(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('updates', {
            parent: 'entity',
            url: '/updates',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Updates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/updates/updates.html',
                    controller: 'UpdatesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('updates-detail', {
            parent: 'updates',
            url: '/updates/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Updates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/updates/updates-detail.html',
                    controller: 'UpdatesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Updates', function($stateParams, Updates) {
                    return Updates.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'updates',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('updates-detail.edit', {
            parent: 'updates-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/updates/updates-dialog.html',
                    controller: 'UpdatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Updates', function(Updates) {
                            return Updates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('updates.new', {
            parent: 'updates',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/updates/updates-dialog.html',
                    controller: 'UpdatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entityName: null,
                                entityId: null,
                                updateType: null,
                                updateDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('updates', null, { reload: 'updates' });
                }, function() {
                    $state.go('updates');
                });
            }]
        })
        .state('updates.edit', {
            parent: 'updates',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/updates/updates-dialog.html',
                    controller: 'UpdatesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Updates', function(Updates) {
                            return Updates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('updates', null, { reload: 'updates' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('updates.delete', {
            parent: 'updates',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/updates/updates-delete-dialog.html',
                    controller: 'UpdatesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Updates', function(Updates) {
                            return Updates.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('updates', null, { reload: 'updates' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
