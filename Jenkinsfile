pipeline {
    agent any

    environment {
        APP_MODULE = 'app'
        BUILD_VARIANT = 'Debug'
        ARTIFACT_DIR = 'artifacts'
        NEWS_API_KEY = credentials('NEWS_API_KEY')
        GRADLE_OPTS       = '-Xmx1g -Xms256m'
        GRADLE_BASE_FLAGS = '--no-daemon --parallel --build-cache'
        LOCAL_PROPERTY = 'local.properties'
    }

    // ─── Pipeline Options ─────────────────────────────────────────────────────
    options {
        timestamps()                        // Show timestamps in build log
        timeout(time: 30, unit: 'MINUTES')  // Kill build if it hangs
        buildDiscarder(logRotator(          // Save storage — keep only last 5 builds
            numToKeepStr: '5',
            artifactNumToKeepStr: '5'
        ))
    }

    parameters {
        string(
          name: "GIT_BRANCH",
          description: "Branch Path",
          defaultValue: 'main',
          trim: true
        )
        string(
          name: "BASE_URL",
          description: "Base API URL",
          defaultValue: 'https://newsapi.org/',
          trim: true
        )
    }

    stages {

        // ── Stage 1: Checkout ─────────────────────────────────────────────────
        stage('Checkout SCM') {
            steps {
                checkout scmGit(
                    branches: [[name: "${params.GIT_BRANCH}"]],
                    extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: 'AUTH-GITHUB-CREDENTIALS',
                        url: 'https://github.com/MAlvinR/NewsClean.git'
                    ]]
                )
            }
        }

        // ── Stage 2: Configure Environment ───────────────────────────────────
        stage('Configure Environment') {
            steps {
                echo '>>> Configuring Environment...'
                // Setup bundler
                sh "gem install bundler"
                sh "bundle install"

                // Grant access so gradle can run the build
                sh "chmod +x gradlew"

                // Overwrite user input to config
                sh "sed -i '' 's#^baseUrl=.*#baseUrl=\"'${params.BASE_URL}'\"#' '${env.LOCAL_PROPERTY}'"

                // Load local.properties to environment
                sh "cp ${LOCAL_PROPERTY} ${env.WORKSPACE}"
                
                // Check version
                sh "java --version"
                sh "gem -v"

                // Printout all parameters
                script {
                    params.keySet().sort().each { key ->
                        echo "${key} = ${params[key]}"
                    }
                }
            }
        }

        // ── Stage 3: Build APK ────────────────────────────────────────────────
        stage('Build APK') {
            steps {
                echo ">>> Building ${BUILD_VARIANT} APK via Fastlane..."
                withEnv([
                    "BUILD_VARIANT=${BUILD_VARIANT.toLowerCase()}",
                    "ARTIFACT_TYPE=APK",
                    "NEWS_API_KEY=${NEWS_API_KEY}"
                ]) {
                    sh 'bundle exec fastlane build'
                }
            }
        }

        // ── Stage 4: Archive Artifacts ────────────────────────────────────────
        stage('Archive Artifacts') {
            steps {
                echo ">>> Archiving APK to ${ARTIFACT_DIR}/..."

                // Create the target folder if it doesn't exist
                sh "mkdir -p ${ARTIFACT_DIR}"

                // Copy APK to your specified artifacts folder
                sh "cp app/build/outputs/apk/${BUILD_VARIANT.toLowerCase()}/*.apk ${ARTIFACT_DIR}/"

                // Also archive inside Jenkins so it's accessible via the UI
                archiveArtifacts(
                    artifacts: "${ARTIFACT_DIR}/*.apk",
                    fingerprint: true,
                    allowEmptyArchive: false
                )

                echo ">>> APK archived successfully."
            }
        }

        // Parallel Quality Gate
        stage('Quality Gate') {
          parallel {
            stage('Check Lint') {
              steps {
                echo '>>> Running Lint checks...'
                sh "./gradlew ${GRADLE_BASE_FLAGS} ktlintCheck"
              }
            }

            stage('Run Unit Tests') {
              steps {
                echo  '>>> Running Unit tests...'
                sh "./gradlew ${GRADLE_BASE_FLAGS} testDebugUnitTest"
              }
            }
          }
        }

//         // Lint
//         stage('Check Lint') {
//            steps {
//               echo  '>>> Running Lint checks...'
//               sh "./gradlew ktlintCheck"
//            }
//         }

//         // Unit Test
//         stage('Run Unit Tests') {
//             steps {
//                echo  '>>> Running Unit tests...'
//                sh "./gradlew testDebugUnitTest"
//             }
//         }

//        // UI Test
//         stage('Run UI Tests') {
//             steps {
//                echo  '>>> Running UI tests...'
//                sh "./gradlew ${GRADLE_BASE_FLAGS} :app:connectedDebugAndroidTest"
//             }
//         }
    }

    // ─── Post Build Actions ───────────────────────────────────────────────────
    post {
        success {
            echo "✅ Build SUCCESS — APK is ready in ${ARTIFACT_DIR}/"
        }
        failure {
            echo "❌ Build FAILED — check the logs above for details."
        }
        always {
            echo ">>> Build finished. Cleaning up workspace..."
            cleanWs()   // Wipes workspace after every build — good for saving storage
        }
    }
}