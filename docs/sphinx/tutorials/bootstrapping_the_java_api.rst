.. _bootstrapping_the_java_api:

Bootstrapping the Java API
==========================

Sections
--------

Quick Start Steps
-----------------

The quick-start to building and integrating your namespace with the OpenBEL Framework.

#. Grab the example template from the :ref:`my-file-format` section.
#. Customize the field values.
#. Build your biological entity values with proper encodings.
#. Deploy your namespace to a local (file) or remote (http / https) location.  For file URL format consult `File URI Scheme`_. 
#. Record the URL for this namespace for later.  This uniquely identified your namespace.
#. Retrieve the stock resource index from the URL: http://resource.belframework.org/belframework/1.0/index.xml
#. Update the resource index with your namespace entry.  Use it's URL retrieved in step 5.
#. Store the customized resource index locally using a local file URL.  For file URL format consult `File URI Scheme`_.
#. Open the OpenBEL Framework config/belframework.cfg configuration file and change the 'resource_index_url' to this file URL.
#. Start using your namespace URL in BEL Documents!

.. _gzip: http://www.gzip.org/
.. _Word Characters: http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html#predef
.. _species taxonomy ids: http://www.ncbi.nlm.nih.gov/taxonomy
.. _ISO 8601: http://en.wikipedia.org/wiki/ISO_8601
.. _GK4P: http://www.genenames.org/data/hgnc_data.php?hgnc_id=4295
.. _File URI Scheme: http://en.wikipedia.org/wiki/File_URI_scheme
