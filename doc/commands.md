# Command Configuration

## ./mvnw -Ppre-commit clean install

### Last Execution Duration
- **Duration**: 32584ms (32.6 seconds)
- **Last Updated**: 2025-10-16

### Acceptable Warnings
- None configured yet

### Notes
- OpenRewrite warnings about recipe changes are informational and acceptable as long as no TODO markers are generated
- All LogRecord-related warnings have been suppressed at type level using `// cui-rewrite:disable CuiLogRecordPatternRecipe`

## handle-pull-request

### CI/Sonar Duration
- **Duration**: 300000ms (5 minutes)
- **Last Updated**: 2025-10-16

### Notes
- This duration represents the time to wait for CI and SonarCloud checks to complete
- Includes buffer time for queue delays

### Known Issues
- **SonarCloud Authentication (PR #47, 2025-10-16)**: SonarCloud build failed with HTTP 403 error. Error message: "Failed to query JRE metadata: GET https://api.sonarcloud.io/analysis/jres?os=linux&arch=x86_64 failed with HTTP 403. Please check the property sonar.token or the environment variable SONAR_TOKEN". This is an infrastructure/configuration issue with the SONAR_TOKEN secret that needs to be addressed in the repository settings or GitHub Actions configuration.
