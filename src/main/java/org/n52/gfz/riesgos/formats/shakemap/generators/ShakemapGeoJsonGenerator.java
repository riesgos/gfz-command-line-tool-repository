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
 */

package org.n52.gfz.riesgos.formats.shakemap.generators;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.n52.gfz.riesgos.configuration.parse.defaultformats.DefaultFormatOption;
import org.n52.gfz.riesgos.formats.shakemap.IShakemap;
import org.n52.gfz.riesgos.formats.shakemap.binding.ShakemapXmlDataBinding;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.datahandler.generator.AbstractGenerator;
import org.n52.wps.io.datahandler.generator.GeoJSONGenerator;
import org.n52.wps.webapp.api.FormatEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

/**
 * GeoJson Generator for Shakemaps.
 */
public class ShakemapGeoJsonGenerator extends AbstractGenerator {

    /**
     * Logger to log unexpected behaviour.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ShakemapGeoJsonGenerator.class);

    /**
     * Format for geojson.
     */
    private static final FormatEntry GEOJSON =
            DefaultFormatOption.GEOJSON.getFormat();

    /**
     * Function to convert the shapemap to a feature collection.
     */
    private static final Function<IShakemap, SimpleFeatureCollection>
            TO_FEATURE_COLLECTION =
            CommonTransformationToGeometry.
                    getFunctionToConvertShakemapToFeatureCollection();

    /**
     * Default constructor.
     */
    public ShakemapGeoJsonGenerator() {
        super();

        supportedIDataTypes.add(ShakemapXmlDataBinding.class);
        supportedFormats.add(GEOJSON.getMimeType());
        supportedEncodings.add(GEOJSON.getEncoding());
        formats.add(GEOJSON);
    }

    /**
     * Generates in input stream with the data.
     * @param data data to give back.
     * @param mimeType mime type for the data
     * @param schema schema for the data
     * @return input stream with the data
     * @throws IOException exception that bay be thrown on handling the
     * input stream
     */
    @Override
    public InputStream generateStream(
            final IData data,
            final String mimeType,
            final String schema)
            throws IOException {

        if (data instanceof ShakemapXmlDataBinding) {
            final ShakemapXmlDataBinding binding =
                    (ShakemapXmlDataBinding) data;
            final IShakemap shakemap = binding.getPayloadShakemap();

            final SimpleFeatureCollection featureCollection =
                    TO_FEATURE_COLLECTION.apply(shakemap);

            return new GeoJSONGenerator().generateStream(
                    new GTVectorDataBinding(featureCollection),
                    GEOJSON.getMimeType(),
                    GEOJSON.getSchema());
        } else {
            LOGGER.error(
                    "Can't convert another data "
                            + "binding as ShakemapXmlDataBinding");
        }
        return null;
    }
}
