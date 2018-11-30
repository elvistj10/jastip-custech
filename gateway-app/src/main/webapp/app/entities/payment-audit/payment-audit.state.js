(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-audit', {
            parent: 'entity',
            url: '/payment-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-audit/payment-audits.html',
                    controller: 'PaymentAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-audit-detail', {
            parent: 'payment-audit',
            url: '/payment-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-audit/payment-audit-detail.html',
                    controller: 'PaymentAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PaymentAudit', function($stateParams, PaymentAudit) {
                    return PaymentAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-audit-detail.edit', {
            parent: 'payment-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit/payment-audit-dialog.html',
                    controller: 'PaymentAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentAudit', function(PaymentAudit) {
                            return PaymentAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-audit.new', {
            parent: 'payment-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit/payment-audit-dialog.html',
                    controller: 'PaymentAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entityName: null,
                                entityId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-audit', null, { reload: 'payment-audit' });
                }, function() {
                    $state.go('payment-audit');
                });
            }]
        })
        .state('payment-audit.edit', {
            parent: 'payment-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit/payment-audit-dialog.html',
                    controller: 'PaymentAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentAudit', function(PaymentAudit) {
                            return PaymentAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-audit', null, { reload: 'payment-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-audit.delete', {
            parent: 'payment-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-audit/payment-audit-delete-dialog.html',
                    controller: 'PaymentAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentAudit', function(PaymentAudit) {
                            return PaymentAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-audit', null, { reload: 'payment-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
