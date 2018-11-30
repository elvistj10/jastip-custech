(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('offering-puchase', {
            parent: 'entity',
            url: '/offering-puchase',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingPuchases'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-puchase/offering-puchases.html',
                    controller: 'OfferingPuchaseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('offering-puchase-detail', {
            parent: 'offering-puchase',
            url: '/offering-puchase/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OfferingPuchase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/offering-puchase/offering-puchase-detail.html',
                    controller: 'OfferingPuchaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OfferingPuchase', function($stateParams, OfferingPuchase) {
                    return OfferingPuchase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'offering-puchase',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('offering-puchase-detail.edit', {
            parent: 'offering-puchase-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-puchase/offering-puchase-dialog.html',
                    controller: 'OfferingPuchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingPuchase', function(OfferingPuchase) {
                            return OfferingPuchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-puchase.new', {
            parent: 'offering-puchase',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-puchase/offering-puchase-dialog.html',
                    controller: 'OfferingPuchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                purchased: null,
                                receiptImg: null,
                                receiptImgContentType: null,
                                receiptImgUrl: null,
                                receiptImgThumbUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('offering-puchase', null, { reload: 'offering-puchase' });
                }, function() {
                    $state.go('offering-puchase');
                });
            }]
        })
        .state('offering-puchase.edit', {
            parent: 'offering-puchase',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-puchase/offering-puchase-dialog.html',
                    controller: 'OfferingPuchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfferingPuchase', function(OfferingPuchase) {
                            return OfferingPuchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-puchase', null, { reload: 'offering-puchase' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('offering-puchase.delete', {
            parent: 'offering-puchase',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/offering-puchase/offering-puchase-delete-dialog.html',
                    controller: 'OfferingPuchaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfferingPuchase', function(OfferingPuchase) {
                            return OfferingPuchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('offering-puchase', null, { reload: 'offering-puchase' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
