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

package org.n52.gfz.riesgos.configuration.parse.output.exitvalue;

import org.n52.gfz.riesgos.configuration.IOutputParameter;

/**
 * Interface for a factory to create the identifiers for
 * the supported types to read data from the exit value.
 */
@FunctionalInterface
public interface IExitValueOutputFactory {

    /**
     * Factory method to create the identifier with the given data.
     * @param identifier identifier of the data
     * @param isOptional true if the output is optional
     * @param optionalAbstract optional description of the parameter
     * @return IIdentifierWithBinding
     */
    IOutputParameter create(
            String identifier,
            boolean isOptional,
            String optionalAbstract
    );
}
