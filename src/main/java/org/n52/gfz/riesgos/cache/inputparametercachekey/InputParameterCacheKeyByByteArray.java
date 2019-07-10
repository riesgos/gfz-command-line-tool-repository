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

package org.n52.gfz.riesgos.cache.inputparametercachekey;

import org.n52.gfz.riesgos.cache.IInputParameterCacheKey;

import java.util.Arrays;

/**
 * Key that works with a byte array.
 */
public class InputParameterCacheKeyByByteArray
        implements IInputParameterCacheKey {

    private static final long serialVersionUID = -4274760777940747832L;
    /**
     * Byte array with the content.
     */
    private final byte[] content;

    /**
     * Constructor with a byte array.
     * @param aContent byte array with the content of an idata.
     */
    public InputParameterCacheKeyByByteArray(final byte[] aContent) {
        this.content = aContent;
    }

    /**
     * Tests equality.
     * @param o other object
     * @return true if both are equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputParameterCacheKeyByByteArray that =
                (InputParameterCacheKeyByByteArray) o;
        return Arrays.equals(content, that.content);
    }

    /**
     *
     * @return hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }
}
