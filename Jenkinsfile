pipeline {
	agent any
	options { skipDefaultCheckout(false) }

	stages {
		stage('Checkout') {
			steps {
				git branch: 'develop',
				credentialsId: 'scarlet',
				url: 'https://lab.ssafy.com/s06-final/S06P31D204.git'
			}
	
		}
		stage('ps shutDown') {
			steps {
				sh 'docker-compose -f docker-compose.yml down'
			}

		}

			
		stage('build') {
			 steps {
           			 echo 'Bulid Gradle'
            			dir ('./BE'){
                		sh "./gradlew clean build --exclude-task test"
                
            			}
        		  }
		
		}
		stage('ps restart') {
			steps {
				sh 'docker-compose -f docker-compose.yml up -d'
			}

		}

		

	}

}
