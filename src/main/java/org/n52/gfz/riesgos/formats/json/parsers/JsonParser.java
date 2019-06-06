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

package org.n52.gfz.riesgos.formats.json.parsers;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.n52.gfz.riesgos.formats.IMimeTypeAndSchemaConstants;
import org.n52.gfz.riesgos.formats.json.binding.JsonDataBinding;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.datahandler.parser.AbstractParser;
import org.n52.wps.webapp.api.FormatEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for json input
 */
public class JsonParser extends AbstractParser implements IMimeTypeAndSchemaConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

    /**
     * default constructor
     */
    public JsonParser() {
        super();

        supportedIDataTypes.add(JsonDataBinding.class);
        supportedFormats.add(MIME_TYPE_JSON);
        supportedEncodings.add(DEFAULT_ENCODING);
        formats.add(new FormatEntry(MIME_TYPE_JSON, null, DEFAULT_ENCODING, true));
    }

    @Override
    public IData parse(final InputStream stream, final String mimeType, final String schema) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(stream, byteArrayOutputStream);
            final String content = new String(byteArrayOutputStream.toByteArray());
            final JSONParser parser = new JSONParser();
            final Object parsed = parser.parse(content);
            if(parsed instanceof  JSONObject) {
                final JSONObject jsonObject = (JSONObject) parsed;
                return new JsonDataBinding(jsonObject);
            }
            throw new RuntimeException("Can't parse the content to an json object");
        } catch(final IOException ioException) {
            throw new RuntimeException(ioException);
        } catch(final ParseException parseException) {
            throw new RuntimeException(parseException);
        }
    }
}