package org.ln.noor.tools.rename.service;

import java.util.List;

public record TagToken(String name, List<Object> arguments) implements TemplateComponent { }
