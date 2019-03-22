package org.n52.gfz.riesgos.exitvaluehandler;

/*
 * Copyright (C) 2019 GFZ German Research Centre for Geosciences
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

import org.n52.gfz.riesgos.functioninterfaces.IExitValueHandler;
import org.n52.gfz.riesgos.functioninterfaces.ILogger;

/**
 * Handler for the exit value that logs the value
 */
public class LogExitValueHandler implements IExitValueHandler {

    private final ILogger logger;

    /**
     *
     * @param logger object used to log
     */
    public LogExitValueHandler(final ILogger logger) {
        this.logger = logger;
    }

    @Override
    public void handleExitValue(int exitValue) {
        logger.log("Exit value: " + exitValue);
    }
}