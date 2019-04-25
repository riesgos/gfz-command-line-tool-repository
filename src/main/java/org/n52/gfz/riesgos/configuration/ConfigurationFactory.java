package org.n52.gfz.riesgos.configuration;

/*
 * Copyright (C) 2019 GFZ German Research Centre for Geosciences
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the Licence is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the Licence for the specific language governing permissions and
 *  limitations under the Licence.
 *
 *

 */

import org.apache.commons.io.IOUtils;
import org.n52.gfz.riesgos.configuration.parse.IParseConfiguration;
import org.n52.gfz.riesgos.configuration.parse.json.ParseJsonConfigurationImpl;
import org.n52.gfz.riesgos.exceptions.ParseConfigurationException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Factory class for providing predefined configurations
 */
public class ConfigurationFactory {

    private ConfigurationFactory() {
        // static
    }

    /**
     * Creates the configuration for Quakeledger
     * It uses a predefined docker image (quakeledger:latest)
     * @return IConfiguration
     */
    public static IConfiguration createQuakeledger() {
        try {
            final InputStream inputStream = ConfigurationFactory.class.getClassLoader().getResourceAsStream("org/n52/gfz/riesgos/configuration/quakeledger.json");
            final String content = new String(IOUtils.toByteArray(inputStream));
            final IParseConfiguration parser = new ParseJsonConfigurationImpl();
            return parser.parse(content);
        } catch(final IOException | ParseConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Creates the configuration for shakyground
     * It uses a predefined docker image (shakyground:latest)
     * @return IConfiguration
     */
    public static IConfiguration createShakyground() {
        try {
            final InputStream inputStream = ConfigurationFactory.class.getClassLoader().getResourceAsStream("org/n52/gfz/riesgos/configuration/shakyground.json");
            final String content = new String(IOUtils.toByteArray(inputStream));
            final IParseConfiguration parser = new ParseJsonConfigurationImpl();
            return parser.parse(content);
        } catch(final IOException | ParseConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }
}