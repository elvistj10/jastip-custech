(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-transfer-history', {
            parent: 'entity',
            url: '/payment-transfer-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-histories.html',
                    controller: 'PaymentTransferHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-transfer-history-detail', {
            parent: 'payment-transfer-history',
            url: '/payment-transfer-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-history-detail.html',
                    controller: 'PaymentTransferHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PaymentTransferHistory', function($stateParams, PaymentTransferHistory) {
                    return PaymentTransferHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-transfer-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-transfer-history-detail.edit', {
            parent: 'payment-transfer-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-history-dialog.html',
                    controller: 'PaymentTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferHistory', function(PaymentTransferHistory) {
                            return PaymentTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-history.new', {
            parent: 'payment-transfer-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-history-dialog.html',
                    controller: 'PaymentTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                postingId: null,
                                offeringId: null,
                                nominal: null,
                                paymentConfirmDateTime: null,
                                checkDateTime: null,
                                expiredDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-history', null, { reload: 'payment-transfer-history' });
                }, function() {
                    $state.go('payment-transfer-history');
                });
            }]
        })
        .state('payment-transfer-history.edit', {
            parent: 'payment-transfer-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-history-dialog.html',
                    controller: 'PaymentTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferHistory', function(PaymentTransferHistory) {
                            return PaymentTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-history', null, { reload: 'payment-transfer-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-history.delete', {
            parent: 'payment-transfer-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-history/payment-transfer-history-delete-dialog.html',
                    controller: 'PaymentTransferHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentTransferHistory', function(PaymentTransferHistory) {
                            return PaymentTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-history', null, { reload: 'payment-transfer-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
