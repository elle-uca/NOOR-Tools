package org.ln.noortools.service;

import java.util.List;

public record TagToken(String name, List<Object> arguments) implements TemplateComponent { }
