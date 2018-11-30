(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-city', {
            parent: 'entity',
            url: '/m-city',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-city/m-cities.html',
                    controller: 'MCityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-city-detail', {
            parent: 'm-city',
            url: '/m-city/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-city/m-city-detail.html',
                    controller: 'MCityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MCity', function($stateParams, MCity) {
                    return MCity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-city',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-city-detail.edit', {
            parent: 'm-city-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-city/m-city-dialog.html',
                    controller: 'MCityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCity', function(MCity) {
                            return MCity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-city.new', {
            parent: 'm-city',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-city/m-city-dialog.html',
                    controller: 'MCityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cityName: null,
                                postalCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-city', null, { reload: 'm-city' });
                }, function() {
                    $state.go('m-city');
                });
            }]
        })
        .state('m-city.edit', {
            parent: 'm-city',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-city/m-city-dialog.html',
                    controller: 'MCityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCity', function(MCity) {
                            return MCity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-city', null, { reload: 'm-city' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-city.delete', {
            parent: 'm-city',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-city/m-city-delete-dialog.html',
                    controller: 'MCityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MCity', function(MCity) {
                            return MCity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-city', null, { reload: 'm-city' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
