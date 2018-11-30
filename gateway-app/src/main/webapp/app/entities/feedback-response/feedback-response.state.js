(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('feedback-response', {
            parent: 'entity',
            url: '/feedback-response',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FeedbackResponses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feedback-response/feedback-responses.html',
                    controller: 'FeedbackResponseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('feedback-response-detail', {
            parent: 'feedback-response',
            url: '/feedback-response/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FeedbackResponse'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feedback-response/feedback-response-detail.html',
                    controller: 'FeedbackResponseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FeedbackResponse', function($stateParams, FeedbackResponse) {
                    return FeedbackResponse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'feedback-response',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('feedback-response-detail.edit', {
            parent: 'feedback-response-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feedback-response/feedback-response-dialog.html',
                    controller: 'FeedbackResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeedbackResponse', function(FeedbackResponse) {
                            return FeedbackResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feedback-response.new', {
            parent: 'feedback-response',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feedback-response/feedback-response-dialog.html',
                    controller: 'FeedbackResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                message: null,
                                responseDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('feedback-response', null, { reload: 'feedback-response' });
                }, function() {
                    $state.go('feedback-response');
                });
            }]
        })
        .state('feedback-response.edit', {
            parent: 'feedback-response',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feedback-response/feedback-response-dialog.html',
                    controller: 'FeedbackResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeedbackResponse', function(FeedbackResponse) {
                            return FeedbackResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feedback-response', null, { reload: 'feedback-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feedback-response.delete', {
            parent: 'feedback-response',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feedback-response/feedback-response-delete-dialog.html',
                    controller: 'FeedbackResponseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FeedbackResponse', function(FeedbackResponse) {
                            return FeedbackResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feedback-response', null, { reload: 'feedback-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
