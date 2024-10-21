package org.vikos.data.processor;

public interface Processor<I, O> {


    /**
     *  Stop at a ETL Processing chain.
     *
     *  Each instance offers filtering and mapping... mapping occur only if filter return True (processable).
     *
     *  Each implementation must be independent of each other accepting and producing immutable data.
     *   -- reusable, concurrently scalable, independent components.
     */

    public boolean filter(I input);
    public O map(I input);
}
