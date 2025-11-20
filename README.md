# NOOR Tools

This project packages a set of desktop utilities built with Spring Boot and Swing.

## Testing notes
- Tests rely on `mockito-inline` to enable inline mock support without warnings. The dependency version is inherited from Spring Boot's managed Mockito BOM through `${mockito.version}`.
- Inline mocks require the Byte Buddy Java agent (`net.bytebuddy:byte-buddy-agent`). The Maven Surefire plugin loads it via `-javaagent` using the version declared in the `bytebuddy.agent.version` property.

These configurations keep the main dependencies aligned with the Spring Boot BOM while avoiding duplicate test module declarations.
