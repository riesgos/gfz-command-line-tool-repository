package org.n52.gfz.riesgos.configuration;

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

import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToGTVectorDataBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToGenericFileDataBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToGenericXMLDataBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToGeotiffBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToJsonDataBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToLiteralStringBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToQuakeMLXmlBinding;
import org.n52.gfz.riesgos.bytetoidataconverter.ConvertBytesToShakemapXmlBinding;
import org.n52.gfz.riesgos.commandlineparametertransformer.BoundingBoxDataToStringCmd;
import org.n52.gfz.riesgos.commandlineparametertransformer.FileToStringCmd;
import org.n52.gfz.riesgos.commandlineparametertransformer.LiteralBooleanBindingToStringCmd;
import org.n52.gfz.riesgos.commandlineparametertransformer.LiteralDoubleBindingToStringCmd;
import org.n52.gfz.riesgos.commandlineparametertransformer.LiteralIntBindingToStringCmd;
import org.n52.gfz.riesgos.commandlineparametertransformer.LiteralStringBindingToStringCmd;
import org.n52.gfz.riesgos.configuration.impl.IdentifierWithBindingImpl;
import org.n52.gfz.riesgos.formats.IMimeTypeAndSchemaConstants;
import org.n52.gfz.riesgos.formats.json.binding.JsonDataBinding;
import org.n52.gfz.riesgos.formats.quakeml.binding.QuakeMLXmlDataBinding;
import org.n52.gfz.riesgos.exitvaluetoidataconverter.ConvertExitValueToLiteralIntBinding;
import org.n52.gfz.riesgos.formats.shakemap.binding.ShakemapXmlDataBinding;
import org.n52.gfz.riesgos.functioninterfaces.ICheckDataAndGetErrorMessage;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertGTVectorDataBindingToBytes;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertGenericFileDataBindingToBytes;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertGenericXMLDataBindingToBytes;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertGenericXMLDataBindingToBytesWithoutHeader;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertGeotiffBindingToBytes;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertJsonDataBindingToBytes;
import org.n52.gfz.riesgos.idatatobyteconverter.ConvertLiteralStringToBytes;
import org.n52.gfz.riesgos.readidatafromfiles.ReadShapeFileFromPath;
import org.n52.gfz.riesgos.readidatafromfiles.ReadSingleByteStreamFromPath;
import org.n52.gfz.riesgos.validators.LiteralStringBindingWithAllowedValues;
import org.n52.gfz.riesgos.validators.XmlBindingWithAllowedSchema;
import org.n52.gfz.riesgos.writeidatatofiles.WriteShapeFileToPath;
import org.n52.gfz.riesgos.writeidatatofiles.WriteSingleByteStreamToPath;
import org.n52.wps.io.data.binding.bbox.BoundingBoxData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.data.binding.complex.GenericFileDataBinding;
import org.n52.wps.io.data.binding.complex.GenericXMLDataBinding;
import org.n52.wps.io.data.binding.complex.GeotiffBinding;
import org.n52.wps.io.data.binding.literal.LiteralBooleanBinding;
import org.n52.wps.io.data.binding.literal.LiteralDoubleBinding;
import org.n52.wps.io.data.binding.literal.LiteralIntBinding;
import org.n52.wps.io.data.binding.literal.LiteralStringBinding;

import java.util.List;
import java.util.UUID;

/**
 * Factory for several predefined kinds of input and output data
 */
public class IdentifierWithBindingFactory {

    private IdentifierWithBindingFactory() {
        // static
    }

    /**
     * Creates a command line int argument
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the data
     * @param flag optional command line flag (--x for a parameter x)
     * @param defaultValue optional default value of the argument
     * @param allowedValues optional list with allowed values
     * @return object with information about how to use the value as a int command line argument input parameter
     */
    public static IIdentifierWithBinding createCommandLineArgumentInt(
            final String identifier,
            final String optionalAbstract,
            final String flag,
            final String defaultValue,
            final List<String> allowedValues) {
        final IdentifierWithBindingImpl.Builder builder = new IdentifierWithBindingImpl.Builder(identifier, LiteralIntBinding.class);
        builder.withAbstract(optionalAbstract);
        builder.withFunctionToTransformToCmd(new LiteralIntBindingToStringCmd(flag));

        if(defaultValue != null) {
            builder.withDefaultValue(defaultValue);
        }

        if(allowedValues != null && (! allowedValues.isEmpty())) {
            builder.withAllowedValues(allowedValues);
        }

        return builder.build();
    }

    /**
     * Creates a command line double argument
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag (--x for a parameter x)
     * @param defaultValue optional default value of the argument
     * @param allowedValues optional list with allowed values
     * @return object with information about how to use the value as a double command line argument input parameter
     */
    public static IIdentifierWithBinding createCommandLineArgumentDouble(
            final String identifier,
            final String optionalAbstract,
            final String flag,
            final String defaultValue,
            final List<String> allowedValues) {
        final IdentifierWithBindingImpl.Builder builder = new IdentifierWithBindingImpl.Builder(identifier, LiteralDoubleBinding.class);
        builder.withAbstract(optionalAbstract);
        builder.withFunctionToTransformToCmd(new LiteralDoubleBindingToStringCmd(flag));

        if(defaultValue != null) {
            builder.withDefaultValue(defaultValue);
        }

        if(allowedValues != null && (! allowedValues.isEmpty())) {
            builder.withAllowedValues(allowedValues);
        }

        return builder.build();
    }

    /**
     * Creates a command line string argument
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag (--x for a parameter x)
     * @param defaultValue optional default value of the argument
     * @param allowedValues optional list with allowed values
     * @return object with information about how to use the value as a string command line argument input parameter
     */
    public static IIdentifierWithBinding createCommandLineArgumentString(
            final String identifier,
            final String optionalAbstract,
            final String flag,
            final String defaultValue,
            final List<String> allowedValues) {
        final IdentifierWithBindingImpl.Builder builder = new IdentifierWithBindingImpl.Builder(identifier, LiteralStringBinding.class);
        builder.withAbstract(optionalAbstract);
        builder.withFunctionToTransformToCmd(new LiteralStringBindingToStringCmd(flag));

        if(defaultValue != null) {
            builder.withDefaultValue(defaultValue);
        }

        if(allowedValues != null && (! allowedValues.isEmpty())) {
            builder.withAllowedValues(allowedValues);
            builder.withValidator(new LiteralStringBindingWithAllowedValues(allowedValues));
        }

        return builder.build();
    }

    /**
     * Creates a command line boolean argument
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag command line flag to insert if the value is true
     * @param defaultValue optional default value
     * @return object with information about how to use the value as a boolean command line argument input parameter
     */
    public static IIdentifierWithBinding createCommandLineArgumentBoolean(
            final String identifier,
            final String optionalAbstract,
            final String flag,
            final String defaultValue) {
        final IdentifierWithBindingImpl.Builder builder = new IdentifierWithBindingImpl.Builder(identifier, LiteralBooleanBinding.class);
        builder.withAbstract(optionalAbstract);
        builder.withFunctionToTransformToCmd(new LiteralBooleanBindingToStringCmd(flag));

        if (defaultValue != null) {
            builder.withDefaultValue(defaultValue);
        }

        return builder.build();
    }


    /**
     * Creates a command line argument bounding box.
     * (This will add four single command line arguments in that order:
     *  lonmin, lonmax, latmin, latmax)
     *
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param supportedCRSForBBox list with the supported CRS for the bounding box
     * @return bounding box command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentBBox(
            final String identifier,
            final String optionalAbstract,
            final List<String> supportedCRSForBBox) {

        return new IdentifierWithBindingImpl.Builder(identifier, BoundingBoxData.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new BoundingBoxDataToStringCmd())
                .withSupportedCRSForBBox(supportedCRSForBBox)
                .build();
    }

    /**
     * Creates a command line argument (xml file) with a file path that will be written down as a temporary file
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param schema schema of the xml
     * @param defaultFlag default flag for the command line argument (for example --file)
     * @return xml file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentXmlFileWithSchema(
            final String identifier,
            final String optionalAbstract,
            final String schema,
            final String defaultFlag) {

        final String filename = createUUIDFilename(".xml");
        final XmlBindingWithAllowedSchema validator;

        if (schema == null || schema.trim().length() == 0) {
            validator = null;
        } else {
            validator = new XmlBindingWithAllowedSchema(schema);
        }

        return new IdentifierWithBindingImpl.Builder(identifier, GenericXMLDataBinding.class)
                .withAbstract(optionalAbstract)
                .withValidator(validator)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, defaultFlag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericXMLDataBindingToBytes()))
                .withSchema(schema)
                .build();
    }

    /**
     * Same as createCommandLineArgumentXmlFileWithSchema, but it removes the xml header before
     * writing it to a file
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param schema schema of the xml
     * @param flag optional flag for the command line argument
     * @return xml file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentXmlFileWithSchemaWithoutHeader(
            final String identifier,
            final String optionalAbstract,
            final String schema,
            final String flag) {

        final String filename = createUUIDFilename(".xml");
        final XmlBindingWithAllowedSchema validator;

        if (schema == null || schema.trim().length() == 0) {
            validator = null;
        } else {
            validator = new XmlBindingWithAllowedSchema(schema);
        }

        return new IdentifierWithBindingImpl.Builder(identifier, GenericXMLDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericXMLDataBindingToBytesWithoutHeader()))
                .withSchema(schema)
                .withValidator(validator)
                .build();
    }

    /**
     * Same as  createCommandLineArgumentXmlFileWithSchema but with QuakeML
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional flag for the command line argument
     * @return quakeml xml file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentQuakeML(
            final String identifier,
            final String optionalAbstract,
            final String flag) {
        final String filename = createUUIDFilename(".xml");

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_QUAKE_ML;

        return new IdentifierWithBindingImpl.Builder(identifier, QuakeMLXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericXMLDataBindingToBytes()))
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates a command line argument (geotiff file) with a file path that will be written down as a
     * temporary file
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag
     * @return geotiff file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentGeotiff(
            final String identifier,
            final String optionalAbstract,
            final String flag) {
        final String filename = createUUIDFilename(".tiff");

        return new IdentifierWithBindingImpl.Builder(identifier, GeotiffBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGeotiffBindingToBytes()))
                .build();
    }

    /**
     * Creates a command line argument (geojson) with a file path that will be written down as a
     * temporary file
     * @param identifier identifier of the data
     * @param optionalDescription optional description of the parameter
     * @param flag optional command line flag
     * @return geojson file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentGeojson(
            final String identifier,
            final String optionalDescription,
            final String flag) {
        final String filename = createUUIDFilename(".json");
        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalDescription)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGTVectorDataBindingToBytes(
                        ConvertGTVectorDataBindingToBytes.Format.JSON
                )))
                .build();
    }

    /**
     * Creates a command line argument (shapefile) with a file path that will be written down as a temporary file
     * (or multiple files, because one shapefile contains multiple files)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag
     * @return shapefile command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentShapeFile(
            final String identifier,
            final String optionalAbstract,
            final String flag) {

        final String filename = createUUIDFilename(".shp");

        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteShapeFileToPath())
                .build();
    }


    /**
     * Creates a command line argument (generic file) with a file path that will be written down as a
     * temporary file
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag
     * @return file command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentFile(
            final String identifier,
            final String optionalAbstract,
            final String flag) {
        final String filename = createUUIDFilename(".dat");

        return new IdentifierWithBindingImpl.Builder(identifier, GenericFileDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericFileDataBindingToBytes()))
                .build();
    }

    /**
     * Creates a command line argument (json file) with a file path that will be written down as
     * a temporary file
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param flag optional command line flag
     * @return json command line argument
     */
    public static IIdentifierWithBinding createCommandLineArgumentJson(
            final String identifier,
            final String optionalAbstract,
            final String flag) {
        final String filename = createUUIDFilename(".json");

        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToTransformToCmd(new FileToStringCmd(filename, flag))
                .withPath(filename)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertJsonDataBindingToBytes()))
                .build();
    }

    /**
     * Creates a stdin input with a string
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param defaultValue optional default value of the argument
     * @param allowedValues optional list with allowed values
     * @return object with information about how to use the value as a string stdin input parameter
     */
    public static IIdentifierWithBinding createStdinString(
            final String identifier,
            final String optionalAbstract,
            final String defaultValue,
            final List<String> allowedValues) {

        final IdentifierWithBindingImpl.Builder builder = new IdentifierWithBindingImpl.Builder(identifier, LiteralStringBinding.class);
        builder.withAbstract(optionalAbstract);
        builder.withFunctionToWriteToStdin(new ConvertLiteralStringToBytes());

        if(defaultValue != null) {
            builder.withDefaultValue(defaultValue);
        }
        if(allowedValues != null && (! allowedValues.isEmpty())) {
            builder.withAllowedValues(allowedValues);
            builder.withValidator(new LiteralStringBindingWithAllowedValues(allowedValues));
        }
        return builder.build();
    }

    /**
     * creates a stdin input with json
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return object with information about how to use the value as a json stdin input parameter
     */
    public static IIdentifierWithBinding createStdinJson(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToWriteToStdin(new ConvertJsonDataBindingToBytes())
                .build();
    }

    /**
     * Creates a input file argument (geotiff file)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before starting the process
     * @return geotiff input file
     */
    public static IIdentifierWithBinding createFileInGeotiff(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GeotiffBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGeotiffBindingToBytes()))
                .build();
    }

    /**
     * Creates a input file argument (geojson file)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before staring the process
     * @return geojson input file
     */
    public static IIdentifierWithBinding createFileInGeojson(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGTVectorDataBindingToBytes(
                        ConvertGTVectorDataBindingToBytes.Format.JSON
                )))
                .build();
    }

    /**
     * Creates a input file argument (shapefile - with all the other files to care about)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before starting the process (just the .shp file)
     * @return shapefile input file
     */
    public static IIdentifierWithBinding createFileInShapeFile(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteShapeFileToPath())
                .build();
    }

    /**
     * Creates an input file argument with quakeml
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before starting the process
     * @return quakeml input file
     */
    public static IIdentifierWithBinding createFileInQuakeML(
            final String identifier,
            final String optionalAbstract,
            final String path) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_QUAKE_ML;

        return new IdentifierWithBindingImpl.Builder(identifier, QuakeMLXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericXMLDataBindingToBytes()))
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates an input file argument with shakemap
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before starting the process
     * @return shakemap input file
     */
    public static IIdentifierWithBinding createFileInShakemap(
            final String identifier,
            final String optionalAbstract,
            final String path) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_SHAKEMAP;

        return new IdentifierWithBindingImpl.Builder(identifier, ShakemapXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericXMLDataBindingToBytes()))
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates an input file argument with json
     * @param identifier identifier of the data
     * @param optinalAbstract optional description of the parameter
     * @param path path of file to write before starting the process
     * @return json input file
     */
    public static IIdentifierWithBinding createFileInJson(
            final String identifier,
            final String optinalAbstract,
            final String path) {

        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optinalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertJsonDataBindingToBytes()))
                .build();
    }


    /**
     * Creates a input file argument (generic)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to write before staring the process
     * @return generic input file
     */
    public static IIdentifierWithBinding createFileInGeneric(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GenericFileDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToWriteToFiles(new WriteSingleByteStreamToPath(new ConvertGenericFileDataBindingToBytes()))
                .build();
    }

    /**
     * Creates a xml file (output) on a given path with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstact optional description of the parameter
     * @param path path of the file to read after process termination
     * @param schema schema of the xml
     * @return output argument containing xml that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutXmlWithSchema(
            final String identifier,
            final String optionalAbstact,
            final String path,
            final String schema) {

        XmlBindingWithAllowedSchema validator;

        if (schema == null || schema.trim().length() == 0) {
            validator = null;
        } else {
            validator = new XmlBindingWithAllowedSchema(schema);
        }

        return new IdentifierWithBindingImpl.Builder(identifier, GenericXMLDataBinding.class)
                .withAbstract(optionalAbstact)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToGenericXMLDataBinding()))
                .withSchema(schema)
                .withValidator(validator)
                .build();
    }

    /**
     * Creates a xml file for quakeml on a given path with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to read after process termination
     * @return output argument containing the quakeml xml that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutQuakeMLFile(
            final String identifier,
            final String optionalAbstract,
            final String path) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_QUAKE_ML;

        return new IdentifierWithBindingImpl.Builder(identifier, QuakeMLXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToQuakeMLXmlBinding()))
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates a xml file for shakemap on a given path with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to read after process termination
     * @return output argument containing the shakemap xml that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutShakemap(
            final String identifier,
            final String optionalAbstract,
            final String path) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_SHAKEMAP;

        return new IdentifierWithBindingImpl.Builder(identifier, ShakemapXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToShakemapXmlBinding()))
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates a xml file for json on a given path
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to read after process termination
     * @return output argument containing the json that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutJson(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToJsonDataBinding()))
                .build();
    }



    /**
     * creates a geotiff file (output) on a given path
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to read after process termination
     * @return output argument containing the geotiff data that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutGeotiff(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GeotiffBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToGeotiffBinding()))
                .build();
    }

    /**
     * Creates a geojson file (output) on a given path
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the file to read after process termination
     * @return output argument containing the geojson data that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutGeojson(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToGTVectorDataBinding(
                        ConvertBytesToGTVectorDataBinding.Format.JSON
                )))
                .build();
    }

    /**
     * Creates a generic file (output) on a given path
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the data
     * @param path path of the file to read after process termination
     * @return output argument containing the data that will be read from a given file
     */
    public static IIdentifierWithBinding createFileOutGeneric(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GenericFileDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadSingleByteStreamFromPath(new ConvertBytesToGenericFileDataBinding()))
                .build();
    }

    /**
     * Creates a shape file (output) on a given path
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param path path of the .shp file to read after process termination
     * @return output argument containing the data that will be read from the given files
     */
    public static IIdentifierWithBinding createFileOutShapeFile(
            final String identifier,
            final String optionalAbstract,
            final String path) {
        return new IdentifierWithBindingImpl.Builder(identifier, GTVectorDataBinding.class)
                .withAbstract(optionalAbstract)
                .withPath(path)
                .withFunctionToReadFromFiles(new ReadShapeFileFromPath())
                .build();
    }

    /**
     * Creates a xml output (via stdout) with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @param schema schema of the xml
     * @return output argument containing xml that will be read from stdout
     */
    public static IIdentifierWithBinding createStdoutXmlWithSchema(
            final String identifier,
            final String optionalAbstract,
            final String schema) {

        final ICheckDataAndGetErrorMessage validator;

        if (schema == null || schema.trim().length() == 0) {
            validator = null;
        } else {
            validator = new XmlBindingWithAllowedSchema(schema);
        }

        return new IdentifierWithBindingImpl.Builder(identifier, GenericXMLDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStdout(new ConvertBytesToGenericXMLDataBinding())
                .withSchema(schema)
                .withValidator(validator)
                .build();
    }

    /**
     * Creates a quakeml xml output (via stdout) with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing quakeml xml that will be read from stdout
     */
    public static IIdentifierWithBinding createStdoutQuakeML(
            final String identifier,
            final String optionalAbstract) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_QUAKE_ML;

        return new IdentifierWithBindingImpl.Builder(identifier, QuakeMLXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStdout(new ConvertBytesToQuakeMLXmlBinding())
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates a shakemap xml output (via stdout) with an additional schema
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the data
     * @return output argument containing shakemap xml that will be read from stdout
     */
    public static IIdentifierWithBinding createStdoutShakemap(
            final String identifier,
            final String optionalAbstract) {

        final String schema = IMimeTypeAndSchemaConstants.SCHEMA_SHAKEMAP;

        return new IdentifierWithBindingImpl.Builder(identifier, ShakemapXmlDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStdout(new ConvertBytesToShakemapXmlBinding())
                .withSchema(schema)
                .withValidator(new XmlBindingWithAllowedSchema(schema))
                .build();
    }

    /**
     * Creates a json output (via stdout)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing json that will be read from stdout
     */
    public static IIdentifierWithBinding createStdoutJson(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStdout(new ConvertBytesToJsonDataBinding())
                .build();
    }

    /**
     * Creates a string output (via stdout)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing the string that will be read from stdout
     */
    public static IIdentifierWithBinding createStdoutString(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, LiteralStringBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStdout(new ConvertBytesToLiteralStringBinding())
                .build();
    }

    /**
     * Creates a string output (via stderr)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing the string that will be read from stderr
     */
    public static IIdentifierWithBinding createStderrString(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, LiteralStringBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStderr(new ConvertBytesToLiteralStringBinding())
                .build();
    }

    /**
     * Creates a json output (via stderr)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing the json that will be read from stderr
     */
    public static IIdentifierWithBinding createStderrJson(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, JsonDataBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleStderr(new ConvertBytesToJsonDataBinding())
                .build();
    }

    /**
     * Creates a int output (via exit value)
     * @param identifier identifier of the data
     * @param optionalAbstract optional description of the parameter
     * @return output argument containing the integer that will be read from exit value
     */
    public static IIdentifierWithBinding createExitValueInt(
            final String identifier,
            final String optionalAbstract) {
        return new IdentifierWithBindingImpl.Builder(identifier, LiteralIntBinding.class)
                .withAbstract(optionalAbstract)
                .withFunctionToHandleExitValue(new ConvertExitValueToLiteralIntBinding())
                .build();
    }

    /*
     * creates a unique filename
     */
    private static String createUUIDFilename(final String ending) {
        final String prefix = "inputfile";
        return prefix + UUID.randomUUID() + ending;
    }
}
