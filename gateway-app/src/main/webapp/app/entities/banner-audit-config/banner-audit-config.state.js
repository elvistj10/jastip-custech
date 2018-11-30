(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('banner-audit-config', {
            parent: 'entity',
            url: '/banner-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BannerAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-configs.html',
                    controller: 'BannerAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('banner-audit-config-detail', {
            parent: 'banner-audit-config',
            url: '/banner-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BannerAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-config-detail.html',
                    controller: 'BannerAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BannerAuditConfig', function($stateParams, BannerAuditConfig) {
                    return BannerAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'banner-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('banner-audit-config-detail.edit', {
            parent: 'banner-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-config-dialog.html',
                    controller: 'BannerAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BannerAuditConfig', function(BannerAuditConfig) {
                            return BannerAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner-audit-config.new', {
            parent: 'banner-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-config-dialog.html',
                    controller: 'BannerAuditConfigDialogController',
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
                    $state.go('banner-audit-config', null, { reload: 'banner-audit-config' });
                }, function() {
                    $state.go('banner-audit-config');
                });
            }]
        })
        .state('banner-audit-config.edit', {
            parent: 'banner-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-config-dialog.html',
                    controller: 'BannerAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BannerAuditConfig', function(BannerAuditConfig) {
                            return BannerAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner-audit-config', null, { reload: 'banner-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner-audit-config.delete', {
            parent: 'banner-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit-config/banner-audit-config-delete-dialog.html',
                    controller: 'BannerAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BannerAuditConfig', function(BannerAuditConfig) {
                            return BannerAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner-audit-config', null, { reload: 'banner-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
