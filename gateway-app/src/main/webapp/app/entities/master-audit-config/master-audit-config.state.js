(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('master-audit-config', {
            parent: 'entity',
            url: '/master-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MasterAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/master-audit-config/master-audit-configs.html',
                    controller: 'MasterAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('master-audit-config-detail', {
            parent: 'master-audit-config',
            url: '/master-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MasterAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/master-audit-config/master-audit-config-detail.html',
                    controller: 'MasterAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MasterAuditConfig', function($stateParams, MasterAuditConfig) {
                    return MasterAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'master-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('master-audit-config-detail.edit', {
            parent: 'master-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit-config/master-audit-config-dialog.html',
                    controller: 'MasterAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MasterAuditConfig', function(MasterAuditConfig) {
                            return MasterAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('master-audit-config.new', {
            parent: 'master-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit-config/master-audit-config-dialog.html',
                    controller: 'MasterAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entityName: null,
                                activeStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('master-audit-config', null, { reload: 'master-audit-config' });
                }, function() {
                    $state.go('master-audit-config');
                });
            }]
        })
        .state('master-audit-config.edit', {
            parent: 'master-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit-config/master-audit-config-dialog.html',
                    controller: 'MasterAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MasterAuditConfig', function(MasterAuditConfig) {
                            return MasterAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('master-audit-config', null, { reload: 'master-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('master-audit-config.delete', {
            parent: 'master-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit-config/master-audit-config-delete-dialog.html',
                    controller: 'MasterAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MasterAuditConfig', function(MasterAuditConfig) {
                            return MasterAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('master-audit-config', null, { reload: 'master-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
