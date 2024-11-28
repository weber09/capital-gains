package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;

import java.util.List;

public interface SellOperationHandler {
    Tax handle(Operation operation, CapitalGainsContext context);
}
