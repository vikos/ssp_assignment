package org.vikos.data;

// @ASSUMPTION: All property has the same language ...
public record SKU(Property<String> productId, Property<String> variantId, Property<String> name, Brand brand, Size size, Property<Category> category, String language) {

    // Holds the processed and raw value together so the whole processing chain sees the source data (without knowing the CSV structure).
    public record Property<T>(T value, String rawValue) { }

    public record Brand(String groupName, Property<String> name) { }

    // @TODO: Limit nesting
    // @TODO: Cycle detection
    public record Category(String name, Category parent) { }

    public record Size(Property<String> value, Property<String> type) {}
}
