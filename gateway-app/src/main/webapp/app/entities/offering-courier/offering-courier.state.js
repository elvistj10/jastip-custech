(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering-courier', {
            parent: 'entity',
            url: '/offering-courier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingCouriers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-courier/offering-couriers.html',
                    controller: 'OfferingCourierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-courier-detail', {
            parent: 'offering-courier',
            url: '/offering-courier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingCourier'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-courier/offering-courier-detail.html',
                    controller: 'OfferingCourierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OfferingCourier', function($stateParams, OfferingCourier) {
                    return OfferingCourier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering-courier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-courier-detail.edit', {
            parent: 'offering-courier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-courier/offering-courier-dialog.html',
                    controller: 'OfferingCourierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingCourier', function(OfferingCourier) {
                            return OfferingCourier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-courier.new', {
            parent: 'offering-courier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-courier/offering-courier-dialog.html',
                    controller: 'OfferingCourierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                courierReceiptNo: null,
                                courierReceiptImg: null,
                                courierReceiptImgContentType: null,
                                courierReceiptImgUrl: null,
                                courierReceiptImgThumbUrl: null,
                                courierSendDate: null,
                                courierDeliveryDay: null,
                                courierEstDeliveredDate: null,
                                confirmReceivedDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering-courier', null, { reload: 'offering-courier' });
                }, function() {
                    $state.go('offering-courier');
                });
            }]
        })
        .state('offering-courier.edit', {
            parent: 'offering-courier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-courier/offering-courier-dialog.html',
                    controller: 'OfferingCourierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingCourier', function(OfferingCourier) {
                            return OfferingCourier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-courier', null, { reload: 'offering-courier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-courier.delete', {
            parent: 'offering-courier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-courier/offering-courier-delete-dialog.html',
                    controller: 'OfferingCourierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferingCourier', function(OfferingCourier) {
                            return OfferingCourier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-courier', null, { reload: 'offering-courier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
