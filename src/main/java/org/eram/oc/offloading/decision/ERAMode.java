package org.eram.oc.offloading.decision;

import org.eram.common.Clone;
import org.eram.core.app.Application;
import org.eram.core.app.Task;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ERAMode: default Offloading decision engine.
 */

public class ERAMode implements ODE {

    @Override
    public Map<String, Clone> decide(Application app, Set<Clone> availableClone) {

        Map<String, Clone> assignMap = new LinkedHashMap<>();

        Clone clone = availableClone.iterator().next();

        for(Task task: app.getTasks()){

            if(app.canOffload(task))
                assignMap.put(task.toString(), clone);
            else
                assignMap.put(task.toString(), null);
        }

        return assignMap;
    }
}
