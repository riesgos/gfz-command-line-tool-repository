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

package org.n52.gfz.riesgos.configuration.parse.input.stdin;

import org.n52.gfz.riesgos.configuration.IInputParameter;
import org.n52.gfz.riesgos.configuration.InputParameterFactory;
import org.n52.gfz.riesgos.configuration.parse.ParseUtils;
import org.n52.gfz.riesgos.exceptions.ParseConfigurationException;
import org.n52.wps.webapp.api.FormatEntry;

import java.util.List;

/**
 * Implementation to create s stdin input with a string.
 */
public class StdinStringFactory implements IAsStdinInputFactory {
    /**
     * Checks some attributes and deligates the creation.
     * @param identifier identifier of the data
     * @param isOptional true if the input is optional
     * @param optionalAbstract optional description of the parameter
     * @param defaultFormat optional default format
     * @param defaultValue optional default value
     * @param allowedValues optional list with allowed values
     * @param schema optional schema
     * @return input parameter
     * @throws ParseConfigurationException exception that may be thrown
     * if there are some values given that the implementation can't use
     */
    @Override
    public IInputParameter create(
            final String identifier,
            final boolean isOptional,
            final String optionalAbstract,
            final FormatEntry defaultFormat,
            final String defaultValue,
            final List<String> allowedValues,
            final String schema)

            throws ParseConfigurationException {
        if (ParseUtils.INSTANCE.strHasValue(schema)) {
            throw new ParseConfigurationException(
                    "schema is not supported for string");
        }

        if (defaultFormat != null) {
            throw new ParseConfigurationException(
                    "defaultFormat is not supported for string");
        }

        return InputParameterFactory.INSTANCE.createStdinString(
                identifier,
                isOptional, optionalAbstract,
                defaultValue,
                allowedValues
        );
    }
}
