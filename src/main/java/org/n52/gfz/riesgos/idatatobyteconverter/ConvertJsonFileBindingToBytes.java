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

package org.n52.gfz.riesgos.idatatobyteconverter;

import org.n52.gfz.riesgos.exceptions.ConvertToBytesException;
import org.n52.gfz.riesgos.formats.jsonfile.binding.JsonFileBinding;
import org.n52.gfz.riesgos.functioninterfaces.IConvertIDataToByteArray;

import java.io.IOException;
import java.util.Objects;

/**
 * Function to convert a json file binding to a byte array.
 */
public class ConvertJsonFileBindingToBytes
        implements IConvertIDataToByteArray<JsonFileBinding> {

    /**
     * Converts the IData to a byte array.
     * @param binding element to convert
     * @return byte array
     */
    @Override
    public byte[] convertToBytes(final JsonFileBinding binding)
        throws ConvertToBytesException {
        try {
            return binding.getPayload().getContent();
        } catch (final IOException ioException) {
            throw new ConvertToBytesException(ioException);
        }
    }

    /**
     *
     * @param o other object
     * @return true if this object equals the other one
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    /**
     *
     * @return hashcode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName());
    }
}
