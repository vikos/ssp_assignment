package org.vikos.data;

import java.util.Optional;

// @TODO: Depth Limit & Cycle detection
public record Category(String name, Optional<Category> parent) {

}