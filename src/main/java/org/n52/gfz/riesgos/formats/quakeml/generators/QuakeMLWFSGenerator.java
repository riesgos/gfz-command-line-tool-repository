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

package org.n52.gfz.riesgos.formats.quakeml.generators;

import org.geotools.feature.FeatureCollection;
import org.n52.gfz.riesgos.configuration.parse.defaultformats.DefaultFormatOption;
import org.n52.gfz.riesgos.exceptions.ConvertFormatException;
import org.n52.gfz.riesgos.formats.IMimeTypeAndSchemaConstants;
import org.n52.gfz.riesgos.formats.quakeml.IQuakeML;
import org.n52.gfz.riesgos.formats.quakeml.QuakeML;
import org.n52.gfz.riesgos.formats.quakeml.binding.QuakeMLXmlDataBinding;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.datahandler.generator.AbstractGenerator;
import org.n52.wps.io.datahandler.generator.GeoserverWFSGenerator;
import org.n52.wps.webapp.api.FormatEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class QuakeMLWFSGenerator extends AbstractGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuakeMLWFSGenerator.class);

    private static final FormatEntry WFS = DefaultFormatOption.WFS.getFormat();

    public QuakeMLWFSGenerator() {
        super();

        supportedIDataTypes.add(QuakeMLXmlDataBinding.class);
        supportedFormats.add(WFS.getMimeType());
        supportedEncodings.add(IMimeTypeAndSchemaConstants.DEFAULT_ENCODING);

        formats.add(WFS);
    }

    @Override
    public InputStream generateStream(
            final IData data,
            final String mimeType,
            final String schema)
        throws IOException {
        if (data instanceof QuakeMLXmlDataBinding) {
            final QuakeMLXmlDataBinding binding = (QuakeMLXmlDataBinding) data;

            try {
                final IQuakeML quakeML = binding.getPayloadQuakeML();

                final FeatureCollection<?, ?> featureCollection = quakeML.toSimpleFeatureCollection();

                return new GeoserverWFSGenerator().generateStream(new GTVectorDataBinding(featureCollection), mimeType, schema);
            } catch (final ConvertFormatException convertFormatException) {
                LOGGER.error("Can't convert the validated quakeml format to WFS");
                LOGGER.error(convertFormatException.toString());
                throw new RuntimeException(convertFormatException);
            }
        } else {
            LOGGER.error("Can't convert another data binding as QuakeMLXmlDataBinding");
        }
        return null;
    }


}
