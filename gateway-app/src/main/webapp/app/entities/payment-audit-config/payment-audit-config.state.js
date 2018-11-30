(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-audit-config', {
            parent: 'entity',
            url: '/payment-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-configs.html',
                    controller: 'PaymentAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-audit-config-detail', {
            parent: 'payment-audit-config',
            url: '/payment-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-config-detail.html',
                    controller: 'PaymentAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PaymentAuditConfig', function($stateParams, PaymentAuditConfig) {
                    return PaymentAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-audit-config-detail.edit', {
            parent: 'payment-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-config-dialog.html',
                    controller: 'PaymentAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentAuditConfig', function(PaymentAuditConfig) {
                            return PaymentAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-audit-config.new', {
            parent: 'payment-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-config-dialog.html',
                    controller: 'PaymentAuditConfigDialogController',
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
                    $state.go('payment-audit-config', null, { reload: 'payment-audit-config' });
                }, function() {
                    $state.go('payment-audit-config');
                });
            }]
        })
        .state('payment-audit-config.edit', {
            parent: 'payment-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-config-dialog.html',
                    controller: 'PaymentAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentAuditConfig', function(PaymentAuditConfig) {
                            return PaymentAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-audit-config', null, { reload: 'payment-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-audit-config.delete', {
            parent: 'payment-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit-config/payment-audit-config-delete-dialog.html',
                    controller: 'PaymentAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentAuditConfig', function(PaymentAuditConfig) {
                            return PaymentAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-audit-config', null, { reload: 'payment-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
