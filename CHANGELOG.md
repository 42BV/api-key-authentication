# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2025-10-31

- Breaking changes: 
  - This version requires Java 21 and Spring Boot 3.5.7+
  - The filter now returns HTTP 401 'Unauthorized' instead of HTTP 403 'Forbidden' when no API Key or an invalid API Key is provided.
  - The filter is now applied using [Path Patterns](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html) instead of Ant Patterns, which are deprecated by Spring.
- Updated dependencies and GitHub actions workflow
- Migrated release workflow to Sonatype Central Publishing

## [2.0.0] - 2023-01-31
- Breaking changes: This version requires Java 17 and Spring Boot 3.0.2+
  - Due to the switch from Java EE to Jakarta EE, this version of the library is incompatible with Spring Boot versions prior to 3.0.0.
  - Spring Boot 3.x also requires Java 17, so Java 11 can therefore no longer be supported.
- Added ability to render a custom error result, in case of missing / invalid API Key.
- Do not include `lombok` as a runtime dependency, its scope should have been `provided`. This is now fixed.
- (unit test dependencies) Replaced deprecated `httpclient` with `httpclient5`.

## [1.0.0] - 2021-12-08
- Initial release

