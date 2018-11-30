(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-province', {
            parent: 'entity',
            url: '/m-province',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MProvinces'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-province/m-provinces.html',
                    controller: 'MProvinceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-province-detail', {
            parent: 'm-province',
            url: '/m-province/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MProvince'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-province/m-province-detail.html',
                    controller: 'MProvinceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MProvince', function($stateParams, MProvince) {
                    return MProvince.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-province',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-province-detail.edit', {
            parent: 'm-province-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-province/m-province-dialog.html',
                    controller: 'MProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MProvince', function(MProvince) {
                            return MProvince.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-province.new', {
            parent: 'm-province',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-province/m-province-dialog.html',
                    controller: 'MProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                provinceName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-province', null, { reload: 'm-province' });
                }, function() {
                    $state.go('m-province');
                });
            }]
        })
        .state('m-province.edit', {
            parent: 'm-province',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-province/m-province-dialog.html',
                    controller: 'MProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MProvince', function(MProvince) {
                            return MProvince.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-province', null, { reload: 'm-province' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-province.delete', {
            parent: 'm-province',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-province/m-province-delete-dialog.html',
                    controller: 'MProvinceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MProvince', function(MProvince) {
                            return MProvince.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-province', null, { reload: 'm-province' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
