# Optimisation Report

## Identified Issues and Fixes

### Bug Fixes
- **Robust delimiter handling in `Word` tag:** special characters in the delimiter list (e.g., `-`, `]`) produced brittle regexes and could raise `PatternSyntaxException`. Replaced the ad-hoc replacement with a dedicated character-class builder that safely escapes regex meta-characters.
  - **Before:** `String regex = "[" + delimiterChars.replaceAll("([\\\\\\]\\[\\-])", "\\\\$1") + "]";`
  - **After:** delimiters are iterated and escaped when required, producing stable patterns.

### Modularization and Duplicate Removal
- **Template file-name logic centralized:** `TemplateApplier` duplicated base-name/extension parsing. Now it reuses `FileNameUtil` for both modes and uses `FileNameUtil.combine` when replacing extensions, reducing divergence between UI logic and utilities.
  - **Before:** local `baseNameOf`/`extensionOf` helpers and manual concatenation.
  - **After:** utility calls keep behaviour consistent across the codebase.

### Testability Improvements
- **Configurable sequence generation:** `NumberSequenceUtil` gained an overload that accepts a `PreferencesService`, decoupling sequence generation from the static Spring context. This enables isolated tests for numeric tags.

### Code Quality and Documentation
- Added class/method level explanations to `TemplateTokenizer`, `TagBuilder`, and `TemplateApplier` to clarify responsibilities.
- Normalised indentation in numeric tags for readability and future maintenance.

## Testing
- Added JUnit 5 coverage for tags and services:
  - `WordTest` covers delimiter parsing and word extraction.
  - `TagFactoryTest` verifies factory lookups.
  - `TemplateTokenizerTest` validates tokenisation paths.
  - `TemplateApplierTest` checks name and extension rewrite flows.
- Maven tests currently fail to resolve the Spring Boot parent POM from Maven Central (HTTP 403), preventing a full test run in this environment.

