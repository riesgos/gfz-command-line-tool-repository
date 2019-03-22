package org.n52.gfz.riesgos.commandlineparametertransformer;

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

import org.n52.gfz.riesgos.exceptions.ConvertToStringCmdException;
import org.n52.gfz.riesgos.functioninterfaces.IConvertIDataToCommandLineParameter;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.literal.LiteralBooleanBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Function to convert an IData to a String.
 * Used to add it as a command line argument.
 * Uses the flag to add it if the value is true.
 */
public class LiteralBooleanBindingToStringCmd implements IConvertIDataToCommandLineParameter {

    private final String commandLineFlag;

    /**
     * Constructor with command line flag
     * @param commandLineFlag flag that is used if the value is true
     */
    public LiteralBooleanBindingToStringCmd(final String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;

    }
    @Override
    public List<String> convertToCommandLineParameter(IData iData) throws ConvertToStringCmdException {

        final List<String> result = new ArrayList<>();

        if(iData instanceof LiteralBooleanBinding) {
            final LiteralBooleanBinding binding = (LiteralBooleanBinding) iData;
            final Boolean value = binding.getPayload();
            if(value) {
                result.add(commandLineFlag);
            }
        } else {
            throw new ConvertToStringCmdException("Wrong binding class");
        }

        return result;
    }
}
