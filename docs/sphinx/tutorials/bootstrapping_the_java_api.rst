.. _bootstrapping_the_java_api:
.. highlight:: java

Bootstrapping the Java API
==========================

System Configuration
--------------------

Creation of the BEL Framework SystemConfiguration_ can be done using one of the
following constructors:

#. SystemConfiguration.createSystemConfiguration()
    * The no-arg method uses the ``BELFRAMEWORK_HOME`` `environment variable`_
      to setup the system configuration. The BEL compiler uses this variant;
      the environment variable is configured in the platform ``setenv`` script.

#. SystemConfiguration.createSystemConfiguration(java.io.File)
    * The file-based method uses a java.io.File_ to setup the system
      configuration. The BEL Framework tools use this variant with the ``-s``
      option.

#. SystemConfiguration.createSystemConfiguration(java.util.Map)
    * The map-based method uses a java.util.Map_ to setup the system
      configuration. This is the preferred method of programmatically
      configuring the BEL Framework for use.

Variable Expansion
^^^^^^^^^^^^^^^^^^

There are a set of variables that will automatically be expanded in the system
configuration when they are seen. These variables can be used in any value of a
name-value pair.

{tmp}
  Expanded to the system temporary directory.
{home}
  Expanded to the user's home directory.
{name}
  Expanded to the user's name.
{dir}
  Expanded to the current working directory.
{belframework_home}
  Expanded to the ``BELFRAMEWORK_HOME`` environment variable.

Programmatic Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^

This is the simplest method of configuring the BEL Framework
SystemConfiguration_ when using the Java API. The SystemConfiguration_ class
exposes a variety of static fields that define the components of the framework's
configuration. See the *belframework.cfg* examples available in the *config*
directory of the BEL Framework.

Example::

    Map<String, String> map = new HashMap<String, String>();
    map.put(SystemConfiguration.KAMSTORE_URL_DESC, "jdbc:mysql://localhost:3306");
    map.put(SystemConfiguration.FRAMEWORK_WORKING_AREA_DESC, "{tmp}/bel_framework");
    // map.put(SystemConfiguration...
    SystemConfiguration syscfg = SystemConfiguration.createSystemConfiguration(map);

.. _SystemConfiguration: http://openbel.github.com/openbel-framework/org/openbel/framework/common/cfg/SystemConfiguration.html
.. _environment variable: http://docs.oracle.com/javase/tutorial/essential/environment/env.html
.. _java.io.File: http://docs.oracle.com/javase/6/docs/api/java/io/File.html
.. _java.util.Map: http://docs.oracle.com/javase/6/docs/api/java/util/Map.html

