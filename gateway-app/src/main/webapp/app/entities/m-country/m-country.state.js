(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-country', {
            parent: 'entity',
            url: '/m-country',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCountries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-country/m-countries.html',
                    controller: 'MCountryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-country-detail', {
            parent: 'm-country',
            url: '/m-country/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCountry'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-country/m-country-detail.html',
                    controller: 'MCountryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MCountry', function($stateParams, MCountry) {
                    return MCountry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-country',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-country-detail.edit', {
            parent: 'm-country-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-country/m-country-dialog.html',
                    controller: 'MCountryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCountry', function(MCountry) {
                            return MCountry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-country.new', {
            parent: 'm-country',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-country/m-country-dialog.html',
                    controller: 'MCountryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                countryName: null,
                                image: null,
                                imageContentType: null,
                                imageUrl: null,
                                imageThumbUrl: null,
                                imageThumbUrlContentType: null,
                                imageFlag: null,
                                imageFlagContentType: null,
                                imageFlagUrl: null,
                                imageFlagThumbUrl: null,
                                countryCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-country', null, { reload: 'm-country' });
                }, function() {
                    $state.go('m-country');
                });
            }]
        })
        .state('m-country.edit', {
            parent: 'm-country',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-country/m-country-dialog.html',
                    controller: 'MCountryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCountry', function(MCountry) {
                            return MCountry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-country', null, { reload: 'm-country' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-country.delete', {
            parent: 'm-country',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-country/m-country-delete-dialog.html',
                    controller: 'MCountryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MCountry', function(MCountry) {
                            return MCountry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-country', null, { reload: 'm-country' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
