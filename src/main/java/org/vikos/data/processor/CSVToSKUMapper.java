package org.vikos.data.processor;

import org.apache.commons.csv.CSVRecord;
import org.vikos.data.SKU;

public class CSVToSKUMapper implements Processor<CSVRecord,SKU> {

    public static final String VARIANT_ID = "variant_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String SIZE_LABEL = "size_label";
    public static final String PRODUCT_NAME = "product_name";
    public static final String BRAND = "brand";
    public static final String COLOR = "color";
    public static final String AGE_GROUP = "age_group";
    public static final String GENDER = "gender";
    public static final String SIZE_TYPE = "size_type";
    public static final String PRODUCT_TYPE = "product_type";



    @Override
    public boolean filter(CSVRecord input) {
        return input.get(VARIANT_ID) != null && input.get(PRODUCT_ID) != null && input.get(PRODUCT_NAME) != null;
    }

    @Override
    public SKU map(CSVRecord record) {

        SKU.Property<String> productId = new SKU.Property<>(record.get(PRODUCT_ID),record.get(PRODUCT_ID));
        SKU.Property<String> variantId = new SKU.Property<>(record.get(VARIANT_ID),record.get(VARIANT_ID));
        SKU.Property<String> name = new SKU.Property<>(record.get(PRODUCT_NAME), record.get(PRODUCT_NAME));
        SKU.Brand brand = new SKU.Brand(null, new SKU.Property<>(record.get(BRAND), record.get(BRAND)));

        String language = null;

        return new SKU(productId, variantId, name, brand, parseSize(record), parseCategory(record), language);
    }

    private SKU.Property<SKU.Category> parseCategory(CSVRecord record) {
        // @todo: Check if " > " is the only delimiter

        String[] breadcrumbs = record.get(PRODUCT_TYPE).split("\s>\s");

        SKU.Category category = null;

        for (String breadcrumb : breadcrumbs) {
            category = new SKU.Category(breadcrumb, category);
        }

        return new SKU.Property<>(category, record.get(PRODUCT_TYPE));
    }

    private SKU.Size parseSize(CSVRecord record) {

        /** Category mapping logic (prouct_tuype).
         *
         * @TODO: Business decision. How to handle multi-word taxonomy elements?
         *  eg: "Clothing > Tops > T-shirts & Jerseys"
         *
         *  Assumption: produt type reffered as
         *
         *
         */
        SKU.Property<String> sizeValue = new SKU.Property<>(record.get(SIZE_LABEL), record.get(SIZE_LABEL));
        SKU.Property<String> sizeType = new SKU.Property<>(record.get(SIZE_TYPE), record.get(SIZE_TYPE));
        return  new SKU.Size(sizeValue, sizeType);
    }

}
