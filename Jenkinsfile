pipeline {
  agent any

  environment {
    TOMCAT_DIR = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
    FE_DIR     = 'HOTELAPI-REACT'
    FE_CONTEXT = 'hotelui'                       // http://<host>:<port>/hotelui
    BE_DIR     = 'HotelApi'
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
        bat 'echo ===== REPO ROOT ===== & dir /b'
      }
    }

    // ===== FRONTEND BUILD =====
    stage('Build Frontend') {
      when { expression { return fileExists("${FE_DIR}/package.json") } }
      steps {
        dir("${FE_DIR}") {
          bat 'echo ===== FRONTEND DIR ===== & cd & dir /b'
          // if you use Jenkins NodeJS tool, wrap these with withNodeJS('YourToolName')
          bat 'node -v & npm -v'
          bat 'npm ci || npm install'
          bat 'npm run build'
          bat 'if exist dist (echo Build output exists in dist) else (echo ERROR: no dist folder & exit /b 1)'
        }
      }
    }

    // ===== FRONTEND DEPLOY =====
    stage('Deploy Frontend to Tomcat') {
      when { expression { return fileExists("${FE_DIR}/dist") } }
      steps {
        bat """
          echo Deploying Frontend to Tomcat...
          if exist "${TOMCAT_DIR}\\webapps\\${FE_CONTEXT}" (
            rmdir /S /Q "${TOMCAT_DIR}\\webapps\\${FE_CONTEXT}"
          )
          mkdir "${TOMCAT_DIR}\\webapps\\${FE_CONTEXT}"
          xcopy /E /I /Y "${FE_DIR}\\dist\\*" "${TOMCAT_DIR}\\webapps\\${FE_CONTEXT}\\"
        """
      }
    }

    // ===== BACKEND BUILD =====
    stage('Build Backend') {
      when { expression { return fileExists("${BE_DIR}/pom.xml") } }
      steps {
        dir("${BE_DIR}") {
          bat 'echo ===== BACKEND DIR ===== & cd & dir /b'
          bat 'mvn -v'
          bat 'mvn -DskipTests clean package'
          bat 'if exist target (echo ===== target ===== & dir /b target)'
        }
      }
    }

    // ===== BACKEND DEPLOY =====
    stage('Deploy Backend to Tomcat') {
      when { expression { return fileExists("${BE_DIR}/target") } }
      steps {
        script {
          // Detect WAR vs JAR
          def warExists = bat(script: "if exist \"${env.BE_DIR}\\target\\*.war\" (echo yes) else (echo no)", returnStdout: true).trim().endsWith('yes')
          if (warExists) {
            bat """
              echo Deploying WAR to Tomcat...
              for %%f in ("${BE_DIR}\\target\\*.war") do (
                echo Found WAR: %%~nxf
                if exist "${TOMCAT_DIR}\\webapps\\%%~nf" (
                  rmdir /S /Q "${TOMCAT_DIR}\\webapps\\%%~nf"
                )
                copy "%%~f" "${TOMCAT_DIR}\\webapps\\"
              )
            """
          } else {
            bat """
              echo No WAR found in ${BE_DIR}\\target.
              echo Backend likely built a JAR (Spring Boot default).
              echo Skipping Tomcat deploy. If you want Tomcat deploy, set <packaging>war</packaging> in pom.xml
              echo and mark spring-boot-starter-tomcat as <scope>provided</scope>.
            """
          }
        }
      }
    }
  }

  post {
    success { echo 'Deployment Successful!' }
    failure { echo 'Pipeline Failed.' }
  }
}
