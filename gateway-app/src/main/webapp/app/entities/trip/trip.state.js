(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trip', {
            parent: 'entity',
            url: '/trip',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Trips'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trip/trips.html',
                    controller: 'TripController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('trip-detail', {
            parent: 'trip',
            url: '/trip/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Trip'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trip/trip-detail.html',
                    controller: 'TripDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Trip', function($stateParams, Trip) {
                    return Trip.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trip',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trip-detail.edit', {
            parent: 'trip-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip/trip-dialog.html',
                    controller: 'TripDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trip', function(Trip) {
                            return Trip.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trip.new', {
            parent: 'trip',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip/trip-dialog.html',
                    controller: 'TripDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                postingDate: null,
                                ownerId: null,
                                timezone: null,
                                originCountryId: null,
                                originCountryName: null,
                                originCityId: null,
                                originCityName: null,
                                destCountryId: null,
                                destCountryName: null,
                                destCityId: null,
                                destCityName: null,
                                startDate: null,
                                endDate: null,
                                postCount: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trip', null, { reload: 'trip' });
                }, function() {
                    $state.go('trip');
                });
            }]
        })
        .state('trip.edit', {
            parent: 'trip',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip/trip-dialog.html',
                    controller: 'TripDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trip', function(Trip) {
                            return Trip.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trip', null, { reload: 'trip' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trip.delete', {
            parent: 'trip',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trip/trip-delete-dialog.html',
                    controller: 'TripDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trip', function(Trip) {
                            return Trip.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trip', null, { reload: 'trip' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
