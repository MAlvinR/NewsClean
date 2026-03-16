pipeline {
    agent any

    environment {
        APP_MODULE = 'app'
        BUILD_VARIANT = 'Debug'
        ARTIFACT_DIR = 'artifacts'
        NEWS_API_KEY = credentials('NEWS_API_KEY')
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

                // Print versions for debugging — helpful when things go wrong
                sh './gradlew --version'
                sh "${ANDROID_HOME}/platform-tools/adb version"

                // Clean previous build outputs
                sh './gradlew clean'
            }
        }

        // ── Stage 3: Build APK ────────────────────────────────────────────────
        stage('Build APK') {
            steps {
                echo ">>> Building ${BUILD_VARIANT} APK..."
                sh './gradlew assemble${BUILD_VARIANT} -PNEWS_API_KEY=${NEWS_API_KEY}'
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