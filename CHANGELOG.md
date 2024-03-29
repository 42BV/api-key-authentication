# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2023-01-31
- Breaking changes: This version requires Java 17 and Spring Boot 3.0.2+
  - Due to the switch from Java EE to Jakarta EE, this version of the library is incompatible with Spring Boot versions prior to 3.0.0.
  - Spring Boot 3.x also requires Java 17, so Java 11 can therefore no longer be supported.
- Added ability to render a custom error result, in case of missing / invalid API Key.
- Do not include `lombok` as a runtime dependency, its scope should have been `provided`. This is now fixed.
- (unit test dependencies) Replaced deprecated `httpclient` with `httpclient5`.

## [1.0.0] - 2021-12-08
- Initial release

