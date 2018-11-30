(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering-payment', {
            parent: 'entity',
            url: '/offering-payment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingPayments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-payment/offering-payments.html',
                    controller: 'OfferingPaymentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-payment-detail', {
            parent: 'offering-payment',
            url: '/offering-payment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingPayment'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-payment/offering-payment-detail.html',
                    controller: 'OfferingPaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OfferingPayment', function($stateParams, OfferingPayment) {
                    return OfferingPayment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering-payment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-payment-detail.edit', {
            parent: 'offering-payment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-payment/offering-payment-dialog.html',
                    controller: 'OfferingPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingPayment', function(OfferingPayment) {
                            return OfferingPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-payment.new', {
            parent: 'offering-payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-payment/offering-payment-dialog.html',
                    controller: 'OfferingPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                finalPriceItem: null,
                                finalServiceFee: null,
                                finalJastipFee: null,
                                uniqueIdentifier: null,
                                finalTotalFee: null,
                                paymentMethod: null,
                                paymentConfirmDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering-payment', null, { reload: 'offering-payment' });
                }, function() {
                    $state.go('offering-payment');
                });
            }]
        })
        .state('offering-payment.edit', {
            parent: 'offering-payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-payment/offering-payment-dialog.html',
                    controller: 'OfferingPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingPayment', function(OfferingPayment) {
                            return OfferingPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-payment', null, { reload: 'offering-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-payment.delete', {
            parent: 'offering-payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-payment/offering-payment-delete-dialog.html',
                    controller: 'OfferingPaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferingPayment', function(OfferingPayment) {
                            return OfferingPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-payment', null, { reload: 'offering-payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
