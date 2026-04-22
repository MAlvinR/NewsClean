pipeline {
    agent any

    environment {
        APP_MODULE = 'app'
        BUILD_VARIANT = 'Debug'
        ARTIFACT_DIR = 'artifacts'
        NEWS_API_KEY = credentials('NEWS_API_KEY')
        GRADLE_OPTS       = '-Xmx1g -Xms256m'
        GRADLE_BASE_FLAGS = '--no-daemon --parallel --build-cache'
        GEM_HOME = "${HOME}/.gem"
        PATH     = "${HOME}/.gem/ruby/2.6.0/bin:${HOME}/.gem/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
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

        // ── Stage 2: Prepare Project ──────────────────────────────────────────
        stage('Prepare Project') {
            steps {
                echo '>>> Preparing project...'

                sh 'chmod +x ./gradlew'

                // Install fastlane directly (with Ruby 2.6 compatible dependency pins)
                sh '''
                    gem install aws-eventstream -v 1.3.2 --user-install --no-document
                    gem install public_suffix -v 4.0.7 --user-install --no-document
                    gem install addressable -v 2.8.1 --user-install --no-document
                    gem install faraday -v 1.10.3 --user-install --no-document
                    gem install fastlane -v 2.214.0 --user-install --no-document
                '''

                // Print versions for debugging
                sh './gradlew --version'
                sh "${ANDROID_HOME}/platform-tools/adb version"
                sh 'fastlane --version'

                // Clean previous build outputs via Fastlane
                sh 'fastlane runClean'
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
                    sh 'fastlane build'
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