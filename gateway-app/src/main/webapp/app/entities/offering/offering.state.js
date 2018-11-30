(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering', {
            parent: 'entity',
            url: '/offering',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Offerings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering/offerings.html',
                    controller: 'OfferingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-detail', {
            parent: 'offering',
            url: '/offering/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Offering'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering/offering-detail.html',
                    controller: 'OfferingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Offering', function($stateParams, Offering) {
                    return Offering.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-detail.edit', {
            parent: 'offering-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering/offering-dialog.html',
                    controller: 'OfferingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Offering', function(Offering) {
                            return Offering.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering.new', {
            parent: 'offering',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering/offering-dialog.html',
                    controller: 'OfferingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                actorId: null,
                                actorType: null,
                                status: null,
                                returnDate: null,
                                sentDate: null,
                                sentFromCountryId: null,
                                sentFromCountryName: null,
                                sentFromCityId: null,
                                sentFromCityName: null,
                                offeringDate: null,
                                timezone: null,
                                offeringExpiredDate: null,
                                quantity: null,
                                priceItem: null,
                                serviceFee: null,
                                jastipFee: null,
                                totalFee: null,
                                tripId: null,
                                tripCityName: null,
                                tripCountryName: null,
                                tripStartDate: null,
                                tripEndDate: null,
                                actorCityFromName: null,
                                shoppingDate: null,
                                deliveryDate: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering', null, { reload: 'offering' });
                }, function() {
                    $state.go('offering');
                });
            }]
        })
        .state('offering.edit', {
            parent: 'offering',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering/offering-dialog.html',
                    controller: 'OfferingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Offering', function(Offering) {
                            return Offering.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering', null, { reload: 'offering' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering.delete', {
            parent: 'offering',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering/offering-delete-dialog.html',
                    controller: 'OfferingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Offering', function(Offering) {
                            return Offering.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering', null, { reload: 'offering' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
