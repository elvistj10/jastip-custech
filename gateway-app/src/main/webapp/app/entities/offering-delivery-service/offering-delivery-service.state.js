(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering-delivery-service', {
            parent: 'entity',
            url: '/offering-delivery-service',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingDeliveryServices'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-services.html',
                    controller: 'OfferingDeliveryServiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-delivery-service-detail', {
            parent: 'offering-delivery-service',
            url: '/offering-delivery-service/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingDeliveryService'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-service-detail.html',
                    controller: 'OfferingDeliveryServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OfferingDeliveryService', function($stateParams, OfferingDeliveryService) {
                    return OfferingDeliveryService.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering-delivery-service',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-delivery-service-detail.edit', {
            parent: 'offering-delivery-service-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-service-dialog.html',
                    controller: 'OfferingDeliveryServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingDeliveryService', function(OfferingDeliveryService) {
                            return OfferingDeliveryService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-delivery-service.new', {
            parent: 'offering-delivery-service',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-service-dialog.html',
                    controller: 'OfferingDeliveryServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sentToCountryId: null,
                                sentToCountryName: null,
                                sentToCityId: null,
                                sentToCityName: null,
                                packageSizeId: null,
                                packageSizeName: null,
                                deliveryServiceId: null,
                                deliveryServiceName: null,
                                deliveryFee: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-service', null, { reload: 'offering-delivery-service' });
                }, function() {
                    $state.go('offering-delivery-service');
                });
            }]
        })
        .state('offering-delivery-service.edit', {
            parent: 'offering-delivery-service',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-service-dialog.html',
                    controller: 'OfferingDeliveryServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingDeliveryService', function(OfferingDeliveryService) {
                            return OfferingDeliveryService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-service', null, { reload: 'offering-delivery-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-delivery-service.delete', {
            parent: 'offering-delivery-service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-service/offering-delivery-service-delete-dialog.html',
                    controller: 'OfferingDeliveryServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferingDeliveryService', function(OfferingDeliveryService) {
                            return OfferingDeliveryService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-service', null, { reload: 'offering-delivery-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
