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
