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

package org.n52.gfz.riesgos.formats.json.generators;

import org.json.simple.JSONObject;
import org.n52.gfz.riesgos.formats.IMimeTypeAndSchemaConstants;
import org.n52.gfz.riesgos.formats.json.binding.JsonDataBinding;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.datahandler.generator.AbstractGenerator;
import org.n52.wps.webapp.api.FormatEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Generator for json data
 */
public class JsonGenerator extends AbstractGenerator implements IMimeTypeAndSchemaConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGenerator.class);

    /**
     * default constructor
     */
    public JsonGenerator() {
        super();

        supportedIDataTypes.add(JsonDataBinding.class);
        supportedFormats.add(MIME_TYPE_JSON);
        supportedEncodings.add(DEFAULT_ENCODING);
        formats.add(new FormatEntry(MIME_TYPE_JSON, null, DEFAULT_ENCODING, true));
    }

    @Override
    public InputStream generateStream(final IData data, final String mimeType, final String schema) throws IOException {
        if(data instanceof JsonDataBinding) {
            final JsonDataBinding binding = (JsonDataBinding) data;
            final JSONObject jsonObject = binding.getPayload();
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonObject.toJSONString().getBytes());
            return inputStream;
        } else {
            LOGGER.error("Can't convert another data binding as JsonDataBinding");
        }
        return null;
    }
}
