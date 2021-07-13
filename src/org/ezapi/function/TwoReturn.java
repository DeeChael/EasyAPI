package org.ezapi.function;

import org.ezapi.returns.DoubleReturn;

@FunctionalInterface
public interface TwoReturn<F,S> extends OneReturn<DoubleReturn<F,S>> {

    @Override
    DoubleReturn<F, S> apply();

}
