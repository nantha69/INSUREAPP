pipeline {

  agent any

  triggers {
    cron('H */8 * * *') //regular builds
    pollSCM('* * * * *') //polling for changes, here once a minute
  }

  stages {
    stage('Checkout and Build') {
      steps { //Checking out the repo
        git credentialsId: '946604fa-eead-44ba-8bcb-18da36e469d5', url: 'https://github.com/symtrain-eng/symdevops.git'
        script {
          sh './gradlew clean build -x check -x test --no-daemon' //run a gradle task
        }
      }
    }
    stage('Static Analysis') {
      steps {
        script {
          sh './gradlew check -x test --no-daemon' //run a gradle task
        }
        publishHTML (target : [allowMissing: false,
                               alwaysLinkToLastBuild: true,
                               keepAll: false,
                               reportDir: 'build/reports/spotbugs',
                               reportFiles: 'main.html',
                               reportName: 'SpotBugs Report',
                               reportTitles: 'SpotBugs Report'])
      }
    }
    stage('Unit/Integration Tests') {
      steps {
        script {
          sh './gradlew test -x check --no-daemon' //run a gradle task
        }
        publishHTML (target : [allowMissing: false,
                               alwaysLinkToLastBuild: true,
                               keepAll: false,
                               reportDir: 'build/reports/tests/test',
                               reportFiles: 'index.html',
                               reportName: 'Junit Report',
                               reportTitles: 'Junit Report'])
      }
    }
    stage('Uploading package to ECR') {
      steps {
        script {
          //configure registry
          docker.withRegistry('http://670084876636.dkr.ecr.ap-southeast-1.amazonaws.com/aws-microservice-demo') {

            //build image
            def customImage = docker.build("aws-microservice-demo:v_${env.BUILD_NUMBER}")

            //push image
            customImage.push()
          }
        }
      }
    }
    stage('Deploying to ECS') {
      steps {
        script {
          sh '''#!/bin/bash
                            set -x
                            sudo groupadd docker
                            sudo usermod -aG docker $USER
                            chmod 777 /var/run/docker.sock
                            PATH=$PATH:/usr/local/bin; export PATH
                            REGION=ap-southeast-1
                            ECR_REPO="670084876636.dkr.ecr.ap-southeast-1.amazonaws.com/aws-microservice-demo"
                            #$(aws ecr get-login --region ${REGION})
                            aws ecr get-login --no-include-email --region ${REGION}>>login.sh
                            sh login.sh'''
        }
        script {
          sh '''#!/bin/bash
                        set -x
                        #Constants
                        PATH=$PATH:/usr/local/bin; export PATH
                        REGION=ap-southeast-1
                        REPOSITORY_NAME=aws-microservice-demo
                        CLUSTER=DefaultEcsCluster
                        FAMILY=`sed -n \'s/.*"family": "\\(.*\\)",/\\1/p\' taskdef.json`
                        NAME=`sed -n \'s/.*"name": "\\(.*\\)",/\\1/p\' taskdef.json`
                        SERVICE_NAME=${NAME}-service
                        env
                        aws configure list
                        echo $HOME
                        #Store the repositoryUri as a variable
                        REPOSITORY_URI=`aws ecr describe-repositories --repository-names ${REPOSITORY_NAME} --region ${REGION} | jq .repositories[].repositoryUri | tr -d \'"\'`
                        #Replace the build number and respository URI placeholders with the constants above
                        sed -e "s;%BUILD_NUMBER%;${BUILD_NUMBER};g" -e "s;%REPOSITORY_URI%;${REPOSITORY_URI};g" taskdef.json > ${NAME}-v_${BUILD_NUMBER}.json
                        #Register the task definition in the repository
                        aws ecs register-task-definition --family ${FAMILY} --cli-input-json file://${WORKSPACE}/${NAME}-v_${BUILD_NUMBER}.json --region ${REGION}
                        SERVICES=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER} --region ${REGION} | jq .failures[]`
                        #Get latest revision
                        REVISION=`aws ecs describe-task-definition --task-definition ${NAME} --region ${REGION} | jq .taskDefinition.revision`
                        #Create or update service
                        if [ "$SERVICES" == "" ]; then
                          echo "entered existing service"
                          DESIRED_COUNT=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER} --region ${REGION} | jq .services[].desiredCount`
                          if [ ${DESIRED_COUNT} = "0" ]; then
                            DESIRED_COUNT="1"
                          fi
                          aws ecs update-service --cluster ${CLUSTER} --region ${REGION} --service ${SERVICE_NAME} --task-definition ${FAMILY}:${REVISION} --desired-count ${DESIRED_COUNT}
                        else
                          echo "entered new service"
                          aws ecs create-service --service-name ${SERVICE_NAME} --desired-count 1 --task-definition ${FAMILY} --cluster ${CLUSTER} --region ${REGION}
                        fi'''
        }
      }
    }

  }
  post {
    always {
      cleanWs()
      //Send an email to the person that broke the build
      step([$class                  : 'Mailer',
            notifyEveryUnstableBuild: true,
            recipients              : [emailextrecipients([[$class: 'CulpritsRecipientProvider'], [$class: 'RequesterRecipientProvider']])].join(' ')])
    }
  }
}
