# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [1.2] - 2024-07-22
### Added
- `build.yml`, `deploy.yml`: converted project to GitHub Actions
### Changes
- `pom.xml`: upgraded Kotlin version to 1.9.24
- `Opt`: temporarily add `equals()` and `hashCode()` while it's not a value class
### Removed
- `.travis.yml`

## [1.1] - 2023-04-11
### Changes
- `Opt`: temporarily change value class to regular class (because of Kotlin bug)

## [1.0] - 2023-02-23
### Added
- all files: initial versions
