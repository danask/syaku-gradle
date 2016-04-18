var appControllers = angular.module('AppControllers',[ ]);

appControllers.controller('IndexCtrl', ['$scope', '$route', '$location', '$http', 
	function($scope, $route, $location, $http) {
		var request = $http({
				method : 'GET',
				url : '/index',
				header : {'accpet' : 'application/json' }
			});

		request.success(function(data) {
			console.log(data);
		});

	}
]);

appControllers.controller('SigninCtrl', ['$rootScope', '$scope', '$route', '$http', '$cookieStore', '$location',
	function($rootScope, $scope, $route, $http, $cookieStore, $location) {

		$scope.fnSignin = function(user) {
			if ($scope.form.$invalid) return;

			delete $rootScope.access_token;
			$cookieStore.remove('access_token');

			var request = $http({
				method : 'POST',
				url : '/login_proc',
				params : user,
				responseType: 'json'
			});

			request.success(function(data, status, headers) {
				$cookieStore.put('access_token', headers("X-Auth-Token"));
				$location.url('/');
			}).error(function(data, status) {
				$rootScope.alert(true, data.message);
			});
		};
	}
]);

appControllers.controller('MypageCtrl', ['$scope', '$route', '$location', '$http',
	function($scope, $route, $location, $http) {

		var request = $http({
			method : 'GET',
			url : '/mypage',
			header : {'accpet' : 'application/json' }
		});

		request.success(function(data) {
			$scope.consumer = data;
		});

	}
]);
