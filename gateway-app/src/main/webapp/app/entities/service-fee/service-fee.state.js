(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-fee', {
            parent: 'entity',
            url: '/service-fee',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ServiceFees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-fee/service-fees.html',
                    controller: 'ServiceFeeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('service-fee-detail', {
            parent: 'service-fee',
            url: '/service-fee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ServiceFee'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-fee/service-fee-detail.html',
                    controller: 'ServiceFeeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ServiceFee', function($stateParams, ServiceFee) {
                    return ServiceFee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-fee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('service-fee-detail.edit', {
            parent: 'service-fee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-fee/service-fee-dialog.html',
                    controller: 'ServiceFeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceFee', function(ServiceFee) {
                            return ServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-fee.new', {
            parent: 'service-fee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-fee/service-fee-dialog.html',
                    controller: 'ServiceFeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                percentageFee: null,
                                fixNominalFee: null,
                                minimumNominalFee: null,
                                minimumNominalPrice: null,
                                maximumNominalPrice: null,
                                startDateTime: null,
                                endDateTime: null,
                                activeStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-fee', null, { reload: 'service-fee' });
                }, function() {
                    $state.go('service-fee');
                });
            }]
        })
        .state('service-fee.edit', {
            parent: 'service-fee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-fee/service-fee-dialog.html',
                    controller: 'ServiceFeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceFee', function(ServiceFee) {
                            return ServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-fee', null, { reload: 'service-fee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-fee.delete', {
            parent: 'service-fee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-fee/service-fee-delete-dialog.html',
                    controller: 'ServiceFeeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceFee', function(ServiceFee) {
                            return ServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-fee', null, { reload: 'service-fee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
