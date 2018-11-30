(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-service-fee', {
            parent: 'entity',
            url: '/m-service-fee',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MServiceFees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-service-fee/m-service-fees.html',
                    controller: 'MServiceFeeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-service-fee-detail', {
            parent: 'm-service-fee',
            url: '/m-service-fee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MServiceFee'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-service-fee/m-service-fee-detail.html',
                    controller: 'MServiceFeeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MServiceFee', function($stateParams, MServiceFee) {
                    return MServiceFee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-service-fee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-service-fee-detail.edit', {
            parent: 'm-service-fee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-service-fee/m-service-fee-dialog.html',
                    controller: 'MServiceFeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MServiceFee', function(MServiceFee) {
                            return MServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-service-fee.new', {
            parent: 'm-service-fee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-service-fee/m-service-fee-dialog.html',
                    controller: 'MServiceFeeDialogController',
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
                    $state.go('m-service-fee', null, { reload: 'm-service-fee' });
                }, function() {
                    $state.go('m-service-fee');
                });
            }]
        })
        .state('m-service-fee.edit', {
            parent: 'm-service-fee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-service-fee/m-service-fee-dialog.html',
                    controller: 'MServiceFeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MServiceFee', function(MServiceFee) {
                            return MServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-service-fee', null, { reload: 'm-service-fee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-service-fee.delete', {
            parent: 'm-service-fee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-service-fee/m-service-fee-delete-dialog.html',
                    controller: 'MServiceFeeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MServiceFee', function(MServiceFee) {
                            return MServiceFee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-service-fee', null, { reload: 'm-service-fee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
