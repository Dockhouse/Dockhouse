(function () {
    'use strict';

    angular
        .module('dockhouseApp')
        .controller('ImageDetailController', ImageDetailController);

    ImageDetailController.$inject = ['$stateParams', '$state', 'Logger', 'Registry'];

    /* @ngInject */
    function ImageDetailController($stateParams, $state, Logger, Registry) {
        /* jshint validthis: true */
        var logger = Logger.getInstance('RegistryDetailController');
        var vm = this;
        var locationIDs = {};

        vm.activate = activate;
        vm.sourceRregistry = {};
        vm.imageTags = "";
        vm.image = {};

        activate();

        ////////////////

        function activate() {
            getLocationIDs();
            checkImageValidity();
            loadSourceRegistry();

            gatherImageData();
            loadImageTags();
        }

        function getLocationIDs() {
            locationIDs = {
                registry : $stateParams.id,
                image : $stateParams.imageID
            };
        }

        function checkImageValidity() {
            /*Registry.testImage(locationIDs.registry, locationIDs.image)
                .catch(function(error) {
                    logger.error('Unavailable image.' + error);
                    $state.go('error');
                });*/
        }

        function loadSourceRegistry() {
            Registry.get(locationIDs.registry)
                .then(function(data){
                    vm.sourceRregistry = data;
                    Registry.testRegistry(vm.sourceRregistry.id)
                        .then(function(data){
                            vm.sourceRregistry.onlineRegistry = data;
                            if(data == "online") {
                            }
                        })
                        .catch(function() {
                            vm.sourceRregistry.onlineRegistry = "offline"; //force offline mode when error occurs.
                        });
                })
                .catch(function(error) {
                    logger.error('Unable to get the given registry.' + error);
                    $state.go('error');
                });
        }

        function gatherImageData() {
        }

        function loadImageTags() {
           /* Registry.getImageTags(locationIDs.registry, locationIDs.image)
                .then(function(data){
                    vm.imageTags = data;
                })
                .catch(function(error) {
                    logger.error('Unable to get tags for the given image.' + error);
                });*/
        }
    }
})();
