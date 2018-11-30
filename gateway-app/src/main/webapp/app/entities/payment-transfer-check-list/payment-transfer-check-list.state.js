(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-transfer-check-list', {
            parent: 'entity',
            url: '/payment-transfer-check-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferCheckLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-lists.html',
                    controller: 'PaymentTransferCheckListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-transfer-check-list-detail', {
            parent: 'payment-transfer-check-list',
            url: '/payment-transfer-check-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PaymentTransferCheckList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-list-detail.html',
                    controller: 'PaymentTransferCheckListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PaymentTransferCheckList', function($stateParams, PaymentTransferCheckList) {
                    return PaymentTransferCheckList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-transfer-check-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-transfer-check-list-detail.edit', {
            parent: 'payment-transfer-check-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-list-dialog.html',
                    controller: 'PaymentTransferCheckListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferCheckList', function(PaymentTransferCheckList) {
                            return PaymentTransferCheckList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-check-list.new', {
            parent: 'payment-transfer-check-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-list-dialog.html',
                    controller: 'PaymentTransferCheckListDialogController',
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
                                expiredDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-check-list', null, { reload: 'payment-transfer-check-list' });
                }, function() {
                    $state.go('payment-transfer-check-list');
                });
            }]
        })
        .state('payment-transfer-check-list.edit', {
            parent: 'payment-transfer-check-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-list-dialog.html',
                    controller: 'PaymentTransferCheckListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentTransferCheckList', function(PaymentTransferCheckList) {
                            return PaymentTransferCheckList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-check-list', null, { reload: 'payment-transfer-check-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-transfer-check-list.delete', {
            parent: 'payment-transfer-check-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-transfer-check-list/payment-transfer-check-list-delete-dialog.html',
                    controller: 'PaymentTransferCheckListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentTransferCheckList', function(PaymentTransferCheckList) {
                            return PaymentTransferCheckList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-transfer-check-list', null, { reload: 'payment-transfer-check-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
