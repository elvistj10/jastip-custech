(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wallet-audit-config', {
            parent: 'entity',
            url: '/wallet-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-configs.html',
                    controller: 'WalletAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('wallet-audit-config-detail', {
            parent: 'wallet-audit-config',
            url: '/wallet-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-config-detail.html',
                    controller: 'WalletAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WalletAuditConfig', function($stateParams, WalletAuditConfig) {
                    return WalletAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wallet-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wallet-audit-config-detail.edit', {
            parent: 'wallet-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-config-dialog.html',
                    controller: 'WalletAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletAuditConfig', function(WalletAuditConfig) {
                            return WalletAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-audit-config.new', {
            parent: 'wallet-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-config-dialog.html',
                    controller: 'WalletAuditConfigDialogController',
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
                    $state.go('wallet-audit-config', null, { reload: 'wallet-audit-config' });
                }, function() {
                    $state.go('wallet-audit-config');
                });
            }]
        })
        .state('wallet-audit-config.edit', {
            parent: 'wallet-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-config-dialog.html',
                    controller: 'WalletAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletAuditConfig', function(WalletAuditConfig) {
                            return WalletAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-audit-config', null, { reload: 'wallet-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-audit-config.delete', {
            parent: 'wallet-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit-config/wallet-audit-config-delete-dialog.html',
                    controller: 'WalletAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WalletAuditConfig', function(WalletAuditConfig) {
                            return WalletAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-audit-config', null, { reload: 'wallet-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
