pipeline {
  agent any

  tools {
    // Configure this in Jenkins: Manage Jenkins -> Global Tool Configuration -> NodeJS
    // Give it a name like "NodeJS_20" and select Node 18/20.
    nodejs 'NodeJS_20'
  }

  environment {
    // Tomcat 10 path – change if yours differs
    TOMCAT_DIR = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
    // Webapp context names (folder names under Tomcat webapps)
    FE_CONTEXT = 'hotelui'               // will deploy to ...\\webapps\\hotelui
    BE_WAR_NAME = 'hotelapi'             // finalName for WAR (optional)
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
        bat 'echo ===== REPO ROOT ===== & dir /b'
      }
    }

    stage('Detect Folders') {
      steps {
        script {
          // Try typical frontend dirs
          def candidatesFE = [
            'HOTELAPI-REACT',
            'STUDENTAPI-REACT',
            'frontend',
            '.'
          ]
          env.FE_DIR = ''
          for (c in candidatesFE) {
            if (fileExists("${c}/package.json")) { env.FE_DIR = c; break }
          }
          echo "Detected FE_DIR='${env.FE_DIR}'"

          // Try typical backend dirs
          def candidatesBE = [
            'HOTELAPI-SPRINGBOOT',
            'STUDENTAPI-SPRINGBOOT',
            'backend',
            '.'
          ]
          env.BE_DIR = ''
          for (c in candidatesBE) {
            if (fileExists("${c}/pom.xml")) { env.BE_DIR = c; break }
          }
          echo "Detected BE_DIR='${env.BE_DIR}'"
        }
      }
    }

    stage('Build Frontend') {
      when { expression { return env.FE_DIR?.trim() && fileExists("${env.FE_DIR}/package.json") } }
      steps {
        dir("${env.FE_DIR}") {
          bat 'echo ===== FRONTEND DIR ===== & cd & dir /b'
          bat 'node -v & npm -v'
          // Prefer reproducible installs; fall back to npm install
          bat 'npm ci || npm install'
          // Vite build -> dist/
          bat 'npm run build'
          bat 'if exist dist (echo Build output exists in dist) else (echo ERROR: no dist folder & exit /b 1)'
        }
      }
    }

    stage('Deploy Frontend to Tomcat') {
      when { expression { return env.FE_DIR?.trim() && fileExists("${env.FE_DIR}/dist") } }
      steps {
        bat """
          echo Deploying Frontend to Tomcat...
          if exist "${env.TOMCAT_DIR}\\webapps\\${env.FE_CONTEXT}" (
            rmdir /S /Q "${env.TOMCAT_DIR}\\webapps\\${env.FE_CONTEXT}"
          )
          mkdir "${env.TOMCAT_DIR}\\webapps\\${env.FE_CONTEXT}"
          xcopy /E /I /Y "${env.FE_DIR}\\dist\\*" "${env.TOMCAT_DIR}\\webapps\\${env.FE_CONTEXT}\\"
        """
      }
    }

    stage('Build Backend') {
      when { expression { return env.BE_DIR?.trim() && fileExists("${env.BE_DIR}/pom.xml") } }
      steps {
        dir("${env.BE_DIR}") {
          bat 'echo ===== BACKEND DIR ===== & cd & dir /b'
          bat 'mvn -v'
          bat 'mvn -DskipTests clean package'
          // show what was built
          bat 'if exist target (echo ===== target ===== & dir /b target)'
        }
      }
    }

    stage('Deploy Backend to Tomcat') {
      when { expression { 
        return env.BE_DIR?.trim() && (fileExists("${env.BE_DIR}/target/*.war") || fileExists("${env.BE_DIR}/target/*.jar"))
      } }
      steps {
        script {
          def warExists = bat(script: "if exist \"${env.BE_DIR}\\target\\*.war\" (echo yes) else (echo no)", returnStdout: true).trim().endsWith('yes')
          if (warExists) {
            // Deploy WAR
            bat """
              echo Deploying WAR to Tomcat...
              for %%f in ("${env.BE_DIR}\\target\\*.war") do (
                echo Found WAR: %%~nxf
                // Clean any previous exploded app folder with same name
                if exist "${env.TOMCAT_DIR}\\webapps\\%%~nf" (
                  rmdir /S /Q "${env.TOMCAT_DIR}\\webapps\\%%~nf"
                )
                copy "%%~f" "${env.TOMCAT_DIR}\\webapps\\"
              )
            """
          } else {
            // JAR detected -> do NOT copy to Tomcat; advise packaging change
            bat """
              echo Backend built as JAR. Skipping Tomcat deploy.
              echo If you want to deploy to Tomcat, change packaging to WAR and add spring-boot-starter-tomcat with provided scope.
            """
          }
        }
      }
    }
  }

  post {
    success { echo 'Deployment Successful!' }
    failure { echo 'Pipeline Failed.' }
    always  {
      echo "FE_DIR=${env.FE_DIR}, BE_DIR=${env.BE_DIR}"
    }
  }
}
