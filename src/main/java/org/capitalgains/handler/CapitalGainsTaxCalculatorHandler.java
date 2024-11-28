package org.capitalgains.handler;

import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;

import java.util.List;

public interface CapitalGainsTaxCalculatorHandler {
    List<Tax> calculateTaxes(List<Operation> operations);
}
