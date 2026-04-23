pipeline {
    agent any

    environment {
        APP_MODULE = 'app'
        BUILD_VARIANT = 'Debug'
        ARTIFACT_DIR = 'artifacts'
        NEWS_API_KEY = credentials('NEWS_API_KEY')
        GRADLE_OPTS       = '-Xmx1g -Xms256m'
        GRADLE_BASE_FLAGS = '--no-daemon --parallel --profile'
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

                // Install bundler directly
                sh 'gem install bundler -v 2.4.22 --user-install --no-document'

                // Print versions for debugging
                sh './gradlew --version'
                sh "${ANDROID_HOME}/platform-tools/adb version"

                // Explicitly invoke our safely-installed local bundler binary
                sh '''
                    BUNDLER_BIN="$HOME/.gem/ruby/2.6.0/bin/bundle"
                    $BUNDLER_BIN install
                '''
            }
        }

        // ── Stage 3: Parallel Quality Gate
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

        // ── Stage 4: Build APK ────────────────────────────────────────────────
        stage('Build APK') {
            steps {
                echo ">>> Building ${BUILD_VARIANT} APK via Fastlane..."
                withEnv([
                    "BUILD_VARIANT=${BUILD_VARIANT.toLowerCase()}",
                    "ARTIFACT_TYPE=APK",
                    "NEWS_API_KEY=${NEWS_API_KEY}"
                ]) {
                    sh '"$HOME/.gem/ruby/2.6.0/bin/bundle" exec fastlane build'
                }
            }
        }

        // ── Stage 5: Archive Artifacts ────────────────────────────────────────
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
            echo ">>> Build finished"
        }
    }
}