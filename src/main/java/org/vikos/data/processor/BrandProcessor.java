package org.vikos.data.processor;

import org.vikos.data.SKU;

public class BrandProcessor implements Processor<SKU, SKU> {

    @Override
    public boolean filter(SKU input) {
        // @TODO: Cross check with other fields such as product name and description ... in case of Brand field is dilluted with other data.
        return input.brand().name() != null;
    }

    @Override
    public SKU map(SKU input) {

        /** @TODO: Detect or Find BrandGroup name
         *
         * Issues to overcome:
         * How to distill brand name from longer text ?
         *
         * @TODO: Identify similar brands from Already inserted DB entries
         *
         * Examples
         * >>> cat opt/raw_product_data.csv | cut -d "," -f 5 | grep Alexander | sort | uniq -c
         *    3229 Adidas Originals By Alexander Wang
         *      13 adidas Originals x Alexander Wang sneakers
         *     131 Alexander Laude
         *       1 Alexander Lewis
         *  298341 Alexander McQueen
         *     112 Alexander McQueen Eyewea
         *     363 Alexander Mcqueen Eyewear
         *    2723 Alexander McQueen Eyewear
         *   15266 Alexander McQueen Kids
         *      39 Alexander McQueen Pre-Owned
         *      65 Alexander McQueen Vintage
         *     251 Alexander Smith
         *      19 Alexander Terekhov
         *   65054 Alexander Wang
         *       9 Alexander Wang vinyl trainers
         *
         *
         * Longest Common Substring:
         *  Pro: Gives precise information about overlapping (start, end positions and length).
         *  Con: Np-hard Extremely cost O(n^2), Not resillient to Typos and single char changes
         *
         * Hamming distance:
         *  Pro: Typo resillient,
         *  Con: Moderaltey costy O(N), No info about the overlap position.
         *
         * Longest Prefix:
         *  Pro: O(1)... easy to implement in SQL (value has to be stored in reverse)
         *  Con: Assumes that all brand name dilutions are suffixes only!
         *
         *  @RECOMMENDED SOLUTION:
         *  Multistage processing:
         *  1.1 Lookup for brands using Longest prefix from the already processed rows.
         *  1.2 Aim for the longest prefix overlap.
         *      Apply filtering based on minimal length and word threshold.
         *      Possible issues with thresholding:  Alexander Wang vs. Alexander McQueen... in comparsion of ADIDAS Shoes vs. ADIDAS Shirts
         *  2. Use Hammond distance over known (in DB) brand If point 1 give no result.
         *     Double top 3-5 candidate with Longest Common Substring.
         *
         *  3. If Step 2 give no result treat as new Brand.
         */

        return null;
    }
}
