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

import org.apache.commons.io.IOUtils;
import org.n52.gfz.riesgos.exceptions.ConvertToBytesException;
import org.n52.gfz.riesgos.functioninterfaces.IConvertIDataToByteArray;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GeotiffBinding;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Implementation to convert a GeotiffBinding to a byte array
 */
public class ConvertGeotiffBindingToBytes implements IConvertIDataToByteArray {

    @Override
    public byte[] convertToBytes(final IData iData) throws ConvertToBytesException {

        if(iData instanceof GeotiffBinding) {
            final GeotiffBinding binding = (GeotiffBinding) iData;
            final File file = binding.getPayload();

            try(final FileReader fileReader = new FileReader(file)) {
                return IOUtils.toByteArray(fileReader);
            } catch(final IOException exception) {
                throw new ConvertToBytesException(exception);
            }
        } else {
            throw new ConvertToBytesException("Wrong binding class");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName());
    }
}
