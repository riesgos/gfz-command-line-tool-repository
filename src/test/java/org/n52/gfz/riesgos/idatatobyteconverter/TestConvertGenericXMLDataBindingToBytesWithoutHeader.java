package org.n52.gfz.riesgos.idatatobyteconverter;

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

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.n52.gfz.riesgos.exceptions.ConvertToBytesException;
import org.n52.gfz.riesgos.functioninterfaces.IConvertIDataToByteArray;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GenericXMLDataBinding;
import org.n52.wps.io.data.binding.literal.LiteralStringBinding;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotEquals;

/**
 * Testcase for ConvertGenericXMLDataBindingToBytesWithoutHeader
 */
public class TestConvertGenericXMLDataBindingToBytesWithoutHeader {

    /**
     * Simple test with just a basic text
     */
    @Test
    public void testValid() {
        try {

            final String input = "<a><b>test</b></a>";

            final IData xmlBinding = new GenericXMLDataBinding(XmlObject.Factory.parse(input));

            final IConvertIDataToByteArray converter = new ConvertGenericXMLDataBindingToBytesWithoutHeader();

            try {
                final byte[] content = converter.convertToBytes(xmlBinding);

                final String strContent = new String(content);

                assertEquals("The converted result is the same as the input", input, strContent);
            } catch(final ConvertToBytesException convertToBytesException) {
                fail("Should not happen");
            }

        } catch(final XmlException xmlException) {
            fail("There should be no xml exception");
        }
    }

    /**
     * If another binding class is used, it should not be possible to convert it to bytes
     * using this converter.
     * There should be an exception.
     */
    @Test
    public void testNonValid() {
        final String input = "<a><b>test</b></a>";

        final IData stringBinding = new LiteralStringBinding(input);

        final IConvertIDataToByteArray converter = new ConvertGenericXMLDataBindingToBytesWithoutHeader();

        try {
            converter.convertToBytes(stringBinding);
            fail("An exception must be thrown");
        } catch(final ConvertToBytesException convertToBytesException) {
            assertNotNull("There must be an exception", convertToBytesException);
        }
    }

    /**
     * Testcase with input that has a header and in the output it mus be removed
     */
    @Test
    public void testInputWithHeader() {
        final String inputWithoutHeader = "<a><b>test</b></a>";
        final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        final String input = header + inputWithoutHeader;

        try {
            final IData xmlBinding = new GenericXMLDataBinding(XmlObject.Factory.parse(input));

            final IConvertIDataToByteArray converter = new ConvertGenericXMLDataBindingToBytesWithoutHeader();

            try {
                final byte[] content = converter.convertToBytes(xmlBinding);

                final String strContent = new String(content);

                assertEquals("The converted result is the same as the input without the header",
                        inputWithoutHeader, strContent);
            } catch(final ConvertToBytesException convertToBytesException) {
                fail("Should not happen");
            }

        } catch(final XmlException xmlException) {
            fail("There should be no xml exception");
        }
    }

    /**
     * Tests equality
     */
    @Test
    public void testEquals() {
        final IConvertIDataToByteArray converter1 = new ConvertGenericXMLDataBindingToBytesWithoutHeader();
        final IConvertIDataToByteArray converter2 = new ConvertGenericXMLDataBindingToBytesWithoutHeader();

        assertEquals("Both are equal", converter1, converter2);

        final IConvertIDataToByteArray converter3 = new ConvertGenericXMLDataBindingToBytes();

        assertNotEquals("The one with a header is different", converter1, converter3);
    }
}
