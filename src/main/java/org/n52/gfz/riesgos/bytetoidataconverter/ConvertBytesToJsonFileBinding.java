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

package org.n52.gfz.riesgos.bytetoidataconverter;

import org.n52.gfz.riesgos.exceptions.ConvertToIDataException;
import org.n52.gfz.riesgos.formats.jsonfile.binding.JsonFileBinding;
import org.n52.gfz.riesgos.formats.jsonfile.binding.JsonFileData;
import org.n52.gfz.riesgos.functioninterfaces.IConvertByteArrayToIData;

import java.io.IOException;
import java.util.Objects;

/**
 * Function to convert the content of a byte array to a JsonFileBinding.
 */
public class ConvertBytesToJsonFileBinding
        implements IConvertByteArrayToIData<JsonFileBinding> {

    private static final long serialVersionUID = 531188643545370771L;

    /**
     * Converts the byte array to an IData element.
     * @param content byte array to convert
     * @return IData element
     * @throws ConvertToIDataException exception if there is an
     * internal error / exception on conversion
     */
    @Override
    public JsonFileBinding convertToIData(final byte[] content)
            throws ConvertToIDataException {
        try {
            return new JsonFileBinding(
                JsonFileData.fromUncompressedBytes(content)
            );
        } catch (final IOException ioException) {
            throw new ConvertToIDataException(ioException);
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
