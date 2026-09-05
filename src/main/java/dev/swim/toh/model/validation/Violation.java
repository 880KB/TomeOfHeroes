package dev.swim.toh.model.validation;

public record Violation(String message, Severity severity, Object source) {
}
