package org.vikos.data.processor;

import org.vikos.data.SKU;

public class LanguageDetector implements Processor<SKU, SKU> {

    /**
     * @TODO: Make bussiness decision: How to treat different languages?
     *   Option A: Translations... So 삼성 equals to Samsung...
     *   Option B: Independent databases... where Language key is mandatory for all core data.
     *   Option C: Semi-Independent? Brands, Categorisation (product_type)... shared..
     *
     *   Answer to this question highly impact the SQL Schema and the whole processing pipeline!
     *
     * @param input
     * @return
     */
    @Override
    public boolean filter(SKU input) {

        // @FIXME: How to handle Mixed language datasets? Korean Product name with English categories (product_type)
        return false;
    }

    @Override
    public SKU map(SKU input) {
        /**
         * @TODO: I would use a Static Language detector lib. Eg: Apache Tikka .. or components of Apache Lucene...
         *
         * @TODO: Add in-memory caching!
         */
        return null;
    }
}
