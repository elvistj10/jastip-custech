(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-transfer-unmatched', {
            parent: 'entity',
            url: '/payment-transfer-unmatched',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferUnmatcheds'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatcheds.html',
                    controller: 'PaymentTransferUnmatchedController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-transfer-unmatched-detail', {
            parent: 'payment-transfer-unmatched',
            url: '/payment-transfer-unmatched/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferUnmatched'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatched-detail.html',
                    controller: 'PaymentTransferUnmatchedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PaymentTransferUnmatched', function($stateParams, PaymentTransferUnmatched) {
                    return PaymentTransferUnmatched.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-transfer-unmatched',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-transfer-unmatched-detail.edit', {
            parent: 'payment-transfer-unmatched-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatched-dialog.html',
                    controller: 'PaymentTransferUnmatchedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferUnmatched', function(PaymentTransferUnmatched) {
                            return PaymentTransferUnmatched.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-unmatched.new', {
            parent: 'payment-transfer-unmatched',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatched-dialog.html',
                    controller: 'PaymentTransferUnmatchedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                postingId: null,
                                offeringId: null,
                                nominal: null,
                                paymentUnmatchedDateTime: null,
                                checkDateTime: null,
                                expiredDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-unmatched', null, { reload: 'payment-transfer-unmatched' });
                }, function() {
                    $state.go('payment-transfer-unmatched');
                });
            }]
        })
        .state('payment-transfer-unmatched.edit', {
            parent: 'payment-transfer-unmatched',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatched-dialog.html',
                    controller: 'PaymentTransferUnmatchedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferUnmatched', function(PaymentTransferUnmatched) {
                            return PaymentTransferUnmatched.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-unmatched', null, { reload: 'payment-transfer-unmatched' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-unmatched.delete', {
            parent: 'payment-transfer-unmatched',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-unmatched/payment-transfer-unmatched-delete-dialog.html',
                    controller: 'PaymentTransferUnmatchedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentTransferUnmatched', function(PaymentTransferUnmatched) {
                            return PaymentTransferUnmatched.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-unmatched', null, { reload: 'payment-transfer-unmatched' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
