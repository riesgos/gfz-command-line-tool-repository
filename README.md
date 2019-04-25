# gfz-riesgos-wps-repository

This is the java source code for the wps-repository for the riesgos project.
Processes that are using command line tools and are used in this project by the GFZ 
should be included here.

## Currently implemented processes

### Quakeledger

Quakeledger is the first services provided here. This implementation is
based on the version provided by Benjamin Proß here:

https://github.com/bpross-52n/quakeledger

This is a slightly modified version of the repository here:

https://github.com/GFZ-Centre-for-Early-Warning/quakeledger

The aim of the quakeledger process is to provide earth quake event informations
in a given region (and with some other filtering criterias (depth, magnitude, ...)).

### Shakyground

Like Quakeledger Shakyground is based on a python script you can find here:

https://github.com/GFZ-Centre-for-Early-Warning/shakyground

It takes an QuakeML input file and computes the shake map (this is xml too).

## Features

All the processes are using a generic template for working with command line tools.
This includes input via
* stdin
* command line parameter
* input files

For output there are the following options:
* handling of the exit value
* handling of stdout
* handling of stderr
* output files

The processes itself run inside of docker containers.
Example Dockerfiles to create the images necessary for this are included in the
assistence folder.

## Installation

1. Make sure that your WPS Server can run docker

For running executables inside of docker it is necessary that docker is installed
on the WPS Server itself.

If the WPS server itself runs inside of docker than you can edit the dockerfile for 
the server by adding the following lines:

```
RUN apt update 
RUN apt install apt-transport-https ca-certificates curl software-properties-common gnupg2 -y
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
RUN apt update 
RUN apt install docker-ce -y
```

Please note that this is the installation process for tomcat:9-jre8 as base image.
This is using a debian stretch distribution. When you use another distribution the 
installation process may change.

If you use docker-compose you must also add a line for adding a volume.
This is used to pass through access to the docker socket:

```
volumes:
    [...]
    - /var/run/docker.sock:/var/run/docker.sock
```

Then rebuild the image used by docker-compose by 
```
docker-compose build
```

2. Compile the project and create a package

The next step is to create a jar file out of the java code:

```
mvn clean package
```

3. Copy the jar to the WEB-INF/lib folder of the server

The created jar must be copied to the WEB-INF/lib folder of the server.

4. Check dependencies

A - maybe - new dependencies that must be included is the jar for Apache Commons Compress.
In the repository here the version 1.9 is used.

Please check if the commons-compress-1.9.jar file is found in the WEB-INF/lib folder
of the server.

If you don't find the jar in that folder you may find it in the 
```
~/.m2/repository/org/apache/commons/commons-compress/1.9/
```
folder. This is the folder used by maven for storing downloaded jar files in an 
Ubuntu 18.10 distribution.
The name of the folder may differ on your system.

Additionally the jar for org.apache.ant and the version 1.10.5 must be provided.
Once you build the new version of this repository you can find the jar in the
m2 repository folder as well.

5. Edit the WEB-INF/classes/dispatcher-servlet.xml file in the 52n-wps-webap-[...] folder

Same as the in the https://github.com/riesgos/52north-wps-osmtovector-process repository
the WEB-INF/classes/dispatcher-servlet.xml file must be edited.

Change the line
```
<context:component-scan base-package="org.n52.wps">
```

into 
```
<context:component-scan base-package="org.n52">
```

This is necessary for the server that it searches for repostoties that are not
part of the org.n52.wps package.

6. Create the docker images for the processes

You must create the docker images for each process you want to use.
At the moment there are the Quakeledger and Shakyground processes.

If you run the WPS Server on a decicated server you must build them on that server.
If you run the WPS Server in docker than it is easily possible to build the images
on the host system (because of sharing the same docker demon).

Go into the assistance/dockerfile/quakeledger folder and run
```
docker build . --tag quakeledger
```

This command needs some time to run.

You need to save the image id for later use.

You can extract it right after the creation by running

```
docker images | head
```

It should be the most recent one.

To build the docker image for shakyground go into the
assistance/dockerfile/shakyground folder and run

```
docker build . --tag shakyground
```


## Configuration

The configuration of the processes is done in json files.
You can take a look at them in the resources/org/n52/gfz/riesgos/configuration
folder.

The json files provide several informations:
| title | This provides the title of the process |
| imageId | ImageID or tag of the docker image to run the script |
| workingDirectory | Directory that is used to run the script inside the docker container |
| commandToExecute | The command to execute in the working directory to run the program in the docker container |
| exitValueHandler | Optional field to add a handler for the exit value |
| stderrHandler | Optional field to add a handler for the stderr output |
| stderrHandler | Optional field to add a handler for the stdout output |
| input | Array of input fields and how they are used |
| output | Array of output fields and where they get the data from |

Example for the quakeledger process:
```javascript

    "title": "QuakeledgerProcess",
    "imageId": "quakeledger:latest",
    "workingDirectory": "/usr/share/git/quakeledger",
    "commandToExecute": "python3 eventquery.py",
    "exitValueHandler": "logging",
    "stderrHandler": "logging",
    "input": [
        { "title" : "input-boundingbox", "useAs": "commandLineArgument", "type": "bbox",   "crs": ["EPSG:4326", "EPSG:4328"]},
        { "title" : "mmin",              "useAs": "commandLineArgument", "type": "double", "default": "6.6"},
        { "title" : "mmax",              "useAs": "commandLineArgument", "type": "double", "default": "8.5"},
        { "title" : "zmin",              "useAs": "commandLineArgument", "type": "double", "default": "5"},
        { "title" : "zmax",              "useAs": "commandLineArgument", "type": "double", "default": "140"},
        { "title" : "p",                 "useAs": "commandLineArgument", "type": "double", "default": "0.1"},
        { "title" : "etype",             "useAs": "commandLineArgument", "type": "string", "default": "deaggregation", "allowed": ["observed", "deaggregation", "stochastic", "expert"]},
        { "title" : "tlon",              "useAs": "commandLineArgument", "type": "double", "default": "-71.5730623712764"},
        { "title" : "tlat",              "useAs": "commandLineArgument", "type": "double", "default": "-33.1299174879672"}
    ],
    "output": [
        { "title": "selectedRows", "readFrom": "file", "path": "test.xml", "type": "xml", "schema": "http://quakeml.org/xmlns/quakeml/1.2/QuakeML-1.2.xsd"}
    ]
}
```

## Exit Value Handler

If no exitValueHandler is provided in the json configuration the exit value of the program will be ignored.
There are the following options for the handler:
#### logging

The exit value will be added to the log file. This is for development and debugging processes.

#### errorIfNotZero

If the exit value is not zero than the process will be terminated without producing output, because the internal script
has thrown an error.

## Stderr Handler

Same as for exit value, the stderr output is ignored by default.

However there are some other handlers

#### logging

The stderr output text will be added to the log file. This is for development and debugging processes.

##### errorIfNotEmpty

If the stderr text is not empty than the process will be terminated without producing output, because the internal
script has thrown an error.

## Stdout Handler

There are no stdout handler at the moment. You can read from stdout by providing an output.

## Supported input

The input field in the configuration must provide an array with the data fields and how they should be given to the
command line program.

Each input must provide a title, and a useAs value.

The following useAs values are possible

#### commandLineArgument

This will be given as a command line argument. The ordering of the elements matters as it is the ordering
the data is given as command line parameter.

The following types are supported via "type" value:
* int
* double
* boolean
* string
* xml
* xmlWithoutHeader
* geotiff
* geojson
* shapefile

For the following types the "flag" attribute is supported:
* int
* double
* string
* boolean (it is mandatory here)
* xml
* xmlWithoutHeader
* geotiff
* geojson
* shapefile

If there is a "flag" attribute then it is inserted before the command line argument.
For example with the flag --etype and the value "expert"
```
[...] --etype "expert"
```

For booleans the flag is mandatory, because no value will be given as command line argument but the flag - but this
only if the value is true.

For the bbox the "crs" attribute must be provided with the list of the supported coordinate reference systems.

For the xml and xmlWithoutHeader types there is an optional "schema" attribute, that is used to validate
the input of the data.

#### stdin

You also can use the stdin as input. At the moment only string as type is supported.

#### file

If you use file as useAs value, than you must provide a "path" attribute.
This path is relative to the working directory.

The following file types are supported:
* geotiff
* geojson
* shapfile

## Supported output

The output field in the configuration must provide an array with the data fields and how they can be read from
the command line program.

Each input must provide a title, and a readFrom value.

The following readFrom values are supported:

#### stdout

You can convert the text from stdout to provide output.
The following values for "type" are supported:

* string
* xml

The xml type can accept an additional "schema" attribute, that is used to validate the output.

#### stderr

Same as stdout it is possible to read from stderr.
Here only the string type is supported.

#### exitValue

You can also read from the exit value.

At the moment only the int type is supported.

#### file

The file value for the "readFrom" attribute needs to have an additional "path" attribute which the file path
of the given output file. This is relative to the working directory.

The following "type" values are supported:
* xml
* geojson
* geotiff
* shapefile

The xml type can also accept an "schema" attribute to validate the output.

## Known problems with docker

Using docker means that there is an overhead in the whole execution process.
A container for each run must be created before and removed after the execution.
For all input and output files there must be communication with the underlying
docker file system. So also temporary files that are already on the server must be
copied to the container.

The overhead may course longer runtimes - and may also course timeouts on 
client side.

Another problem is that the image id differ when created on different machines.
The reason for that is that in most docker build processes there is a kind
of update for a linux package manager. Executed on different machines and in a 
different time forces differences in the files of the system. That means that
the checksums of the layers of the docker build process will differ.


Also the whole process of the installation is much more complicated.

However the use of docker gives some advantages:

* All the process executions are seperated. There is no way that they may influence
each other.
* All temporary files created by the command line executable are removed 
after the execution by removing the docker container.
* Docker separates the dependencies of the command line programs. Each process has
its own Dockerfile, which specifies the dependencies for this single process.
So it is possible to run several services that uses conflicting libraries.
* It is possible to test the execution of a program inside of docker
but outside of the WPS server. Because of docker, running the image will
make sure that the execution will work - even on a different server.

At the moment there is no final decision on running the services in docker or not.

## How to add a service

To add a service the following steps are necessary:

1. Dockerfile

Because the process should run inside of docker you must provide a docker file with the script and
all of the dependencies.

You may use the Dockerfiles for quakeledger and shakyground as templates. You can find them in the
assistance/dockerfiles folder.

2. Build the docker image on the server

You must also build the image on the server.

You normally just have to run

```shell
docker build . --tag newprocess
```
in the folder with the new dockerfile. You may change the the tag of the image to match the name of your process.

3. Check that the input and output you need are supported

At the moment the supported input and output types are created to support the IO in the processes for
quakeledger and shakyground.

You can create an issue on the github page so that we can check how to implement this type.

4. Create a json configuration file

To add a configuration you must provide a json configuration file. You can use the configurations for
quakeledger and shakyground as templates.

5. Tell the server where to find your configuration file

The configuration module for the gfz riesgos wps repository accepts a configuration string with the path of a folder.
You can see this on the administration page for the wps server.

You can change this folder name at any time.

If running the wps server itself in docker, you may need to add a volume to the server.
For example it may be necessary to add the following line to a docker-compose.yml file in the part of the volumes:

```
- ./json-configs:/usr/share/riesgos/json-configurations
```

The path after the colon should match the path if the configuration folder you insert in the wps administration page
for the GFZ RIESGOS Configuration Module.

6. Add your config file in this folder

If you run the wps server inside of docker itself, you can now add your configuration to the json-configs folder.
If you don't run the wps server in docker, you can add your configuration to the folder you inserted in the text
field for the GFZ RIESGOS Configuration Module on the wps administration page.

It is important that your file has an .json ending, because those are the files that are recognized for parsing.

Now your process should be included in the repository.

If you have access to the logs you can check if the parsing is successful and
if there are problems on running your process you can check that too.

7. Change your process (optional)

If you now just want to change your process configuration, you can change the configuration file and it will update on runtime.

If you have to change the dockerfile, than you have to rebuild the image.