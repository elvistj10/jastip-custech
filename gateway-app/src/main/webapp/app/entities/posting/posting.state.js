(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('posting', {
            parent: 'entity',
            url: '/posting',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Postings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/posting/postings.html',
                    controller: 'PostingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('posting-detail', {
            parent: 'posting',
            url: '/posting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Posting'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/posting/posting-detail.html',
                    controller: 'PostingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Posting', function($stateParams, Posting) {
                    return Posting.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'posting',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('posting-detail.edit', {
            parent: 'posting-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posting/posting-dialog.html',
                    controller: 'PostingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Posting', function(Posting) {
                            return Posting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('posting.new', {
            parent: 'posting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posting/posting-dialog.html',
                    controller: 'PostingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                status: null,
                                ownerId: null,
                                postingDate: null,
                                timezone: null,
                                expiredDate: null,
                                productName: null,
                                brand: null,
                                postingItemImg: null,
                                postingItemImgContentType: null,
                                postingItemImgUrl: null,
                                postingItemImgThumbUrl: null,
                                description: null,
                                quantity: null,
                                avaiableQuantity: null,
                                referenceLink: null,
                                itemCategoryId: null,
                                itemCategoryName: null,
                                itemSubCategoryId: null,
                                itemSubCategoryName: null,
                                additionalInfo: null,
                                referencePlace: null,
                                countryBuyFromId: null,
                                countryBuyFromName: null,
                                cityBuyFromId: null,
                                cityBuyFromName: null,
                                countrySentToId: null,
                                countrySentToName: null,
                                citySentToId: null,
                                citySentToName: null,
                                fragile: null,
                                needCooler: null,
                                otherNote: null,
                                otherNoteInfo: null,
                                itemWeight: null,
                                packageSizeId: null,
                                packageSizeName: null,
                                needReceipt: null,
                                priceItem: null,
                                serviceFee: null,
                                jastipFee: null,
                                totalFee: null,
                                acceptedOfferingId: null,
                                shoppingDate: null,
                                deliveryDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('posting', null, { reload: 'posting' });
                }, function() {
                    $state.go('posting');
                });
            }]
        })
        .state('posting.edit', {
            parent: 'posting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posting/posting-dialog.html',
                    controller: 'PostingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Posting', function(Posting) {
                            return Posting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('posting', null, { reload: 'posting' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('posting.delete', {
            parent: 'posting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/posting/posting-delete-dialog.html',
                    controller: 'PostingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Posting', function(Posting) {
                            return Posting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('posting', null, { reload: 'posting' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
