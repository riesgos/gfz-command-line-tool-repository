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

package org.n52.gfz.riesgos.commandlineparametertransformer;

import org.n52.gfz.riesgos.exceptions.ConvertToStringCmdException;
import org.n52.gfz.riesgos.functioninterfaces.IConvertIDataToCommandLineParameter;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.literal.LiteralIntBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Function to convert an IData to a String.
 * Used to add a command line argument.
 * Can also handle default flags.
 */
public class LiteralIntBindingToStringCmd implements IConvertIDataToCommandLineParameter {

    private final String defaultFlag;

    /**
     * Constructor with a default flag
     * @param defaultFlag flag that is before the element (for example --level before a level value)
     */
    public LiteralIntBindingToStringCmd(final String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public LiteralIntBindingToStringCmd() {
        this(null);
    }

    @Override
    public List<String> convertToCommandLineParameter(final IData iData) throws ConvertToStringCmdException {
        final List<String> result = new ArrayList<>();

        Optional.ofNullable(defaultFlag).ifPresent(result::add);

        if(iData instanceof LiteralIntBinding) {
            final LiteralIntBinding binding = (LiteralIntBinding) iData;
            final Integer value = binding.getPayload();
            result.add(String.valueOf(value));
        } else {
            throw new ConvertToStringCmdException("Wrong binding class");
        }

        return result;
    }
}