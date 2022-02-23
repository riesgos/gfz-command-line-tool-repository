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

package org.n52.gfz.riesgos.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Utility functions to work with strings.
 */
public enum StringUtils {

    /**
     * Singleton.
     */
    INSTANCE;

    /**
     * Private constructor for the class that should only be used static.
     */
    StringUtils() {
        // static
    }

    /**
     * Reads the content from a inputStream as String.
     * @param inputStream input Stream (for example from the resources files)
     * @return String from the input stream
     * @throws IOException may throw an IOException
     */
    public static String readFromStream(
            final InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return new String(outputStream.toByteArray());
        }
    }

    /**
     * Reads the content from a resource file.
     * @param path path of the resource file to read
     * @return String from the resource file
     * @throws IOException may throw an IOException
     */
    public static String readFromResourceFile(
            final String path) throws IOException {
        try (InputStream inputStream =
                     StringUtils
                             .class
                             .getClassLoader()
                             .getResourceAsStream(path)) {
            return readFromStream(inputStream);
        }
    }

    /**
     * Create a unique filename.
     * Basically wrote once by 52N, slightly changed.
     * @param fileName existing filename
     * @return some unique filename
     */
    public static String makeUniqueFileName(final String fileName) {
        final int suffixStartIdx = fileName.lastIndexOf('.');
        final boolean hasSuffix = suffixStartIdx >= 0;
        final String suffix = hasSuffix
                ? fileName.substring(suffixStartIdx)
                : "";
        final String rawName = hasSuffix
                ? fileName.substring(0, suffixStartIdx)
                : fileName;
        final int endIndex = 5;
        final String uniquePart =
                UUID.randomUUID().toString().substring(0, endIndex);
        return rawName + "_" + uniquePart + suffix;
    }
}
