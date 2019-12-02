package org.eram.oc.offloading.decision;

import org.eram.common.Clone;
import org.eram.core.app.Application;

import java.util.Map;
import java.util.Set;

/**
 * Offloading Decision Engine.
 */
public interface ODE {

    Map<String, Clone>  decide(Application app, Set<Clone> availableClone);
}
