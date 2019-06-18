package org.n52.gfz.riesgos.validators;

import org.apache.xmlbeans.XmlObject;

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

import org.n52.gfz.riesgos.functioninterfaces.ICheckDataAndGetErrorMessage;
import org.n52.gfz.riesgos.util.XmlSchemaFileTranslator;
import org.n52.wps.io.data.binding.complex.GenericXMLDataBinding;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Validator, that checks that a value is one of some given
 * String values
 */
public class XmlBindingWithAllowedSchema<T extends GenericXMLDataBinding> implements ICheckDataAndGetErrorMessage<T> {

    private final String allowedschema;
    private final List<SAXParseException> validationErrors = new LinkedList<>();

    /**
     * @param value schema for the given xml file
     */
    public XmlBindingWithAllowedSchema(final String value) {
        allowedschema = value;
    }

    @Override
    public Optional<String> check(final T xmlbinding) {
        validationErrors.clear();

        final XmlObject xml = xmlbinding.getPayload();
        final XmlSchemaFileTranslator translator = new XmlSchemaFileTranslator();

        // https://stackoverflow.com/a/16054/2249798
        Object schemaFile = translator.translateUri(allowedschema);

        if (schemaFile == null) {
            return Optional.of("XML schema file could not be loaded: " + validationErrors.toString());
        }

        Source xmlFile = new StreamSource(new StringReader(xml.toString()));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema;

            if (schemaFile instanceof URL) {
                schema = schemaFactory.newSchema((URL) schemaFile);
            } else {
                schema = schemaFactory.newSchema((File) schemaFile);
            }

            Validator validator = schema.newValidator();

            // https://stackoverflow.com/a/11131775/2249798
            validator.setErrorHandler(new ErrorHandler(){
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    validationErrors.add(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    validationErrors.add(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    validationErrors.add(exception);
                }
            });

            validator.validate(xmlFile);
        } catch (SAXException e) {
            return Optional.of("XML file does not validate: " + e);
        } catch (IOException e) {
            return Optional.of("IO error while reading xml or schema");
        }

        if (validationErrors.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of("XML file does not validate: " + validationErrors.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlBindingWithAllowedSchema that = (XmlBindingWithAllowedSchema) o;
        return Objects.equals(allowedschema, that.allowedschema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedschema);
    }
}
