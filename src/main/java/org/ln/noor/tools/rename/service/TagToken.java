package org.ln.noor.tools.rename.service;

import java.util.List;
/**
 * TagToken.
 *
 * @author Luca Noale
 */

public record TagToken(String name, List<Object> arguments) implements TemplateComponent { }
