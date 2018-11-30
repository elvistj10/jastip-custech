(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering-delivery-info', {
            parent: 'entity',
            url: '/offering-delivery-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingDeliveryInfos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-infos.html',
                    controller: 'OfferingDeliveryInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-delivery-info-detail', {
            parent: 'offering-delivery-info',
            url: '/offering-delivery-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingDeliveryInfo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-info-detail.html',
                    controller: 'OfferingDeliveryInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OfferingDeliveryInfo', function($stateParams, OfferingDeliveryInfo) {
                    return OfferingDeliveryInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering-delivery-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-delivery-info-detail.edit', {
            parent: 'offering-delivery-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-info-dialog.html',
                    controller: 'OfferingDeliveryInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingDeliveryInfo', function(OfferingDeliveryInfo) {
                            return OfferingDeliveryInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-delivery-info.new', {
            parent: 'offering-delivery-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-info-dialog.html',
                    controller: 'OfferingDeliveryInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                recipientName: null,
                                phoneNumber: null,
                                deliveryAddress: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-info', null, { reload: 'offering-delivery-info' });
                }, function() {
                    $state.go('offering-delivery-info');
                });
            }]
        })
        .state('offering-delivery-info.edit', {
            parent: 'offering-delivery-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-info-dialog.html',
                    controller: 'OfferingDeliveryInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingDeliveryInfo', function(OfferingDeliveryInfo) {
                            return OfferingDeliveryInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-info', null, { reload: 'offering-delivery-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-delivery-info.delete', {
            parent: 'offering-delivery-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-delivery-info/offering-delivery-info-delete-dialog.html',
                    controller: 'OfferingDeliveryInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferingDeliveryInfo', function(OfferingDeliveryInfo) {
                            return OfferingDeliveryInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-delivery-info', null, { reload: 'offering-delivery-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
