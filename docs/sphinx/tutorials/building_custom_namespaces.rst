Building Custom Namespaces
==========================

Sections
--------

* :ref:`my-definition` defines the importance of a namespace.  A format template is also given.
* :ref:`my-field-format` defines the format of each field.
* :ref:`my-understanding-entity-encoding` explores entity encoding to enforce functional semantics in BEL.
* :ref:`my-quick-start-steps` defines the steps to build and integrate the namespace with your OpenBEL Framework instance.

.. _my-definition:

Definition
----------

A namespace defines a catalog of biological entities useful within the BEL language.  Namespace files end in the *.belns* extension.  The file can be optionally compressed with `gzip`_, but it must use the *.belns.gz* extension.

The namespace allows a user to track their own vocabularies or reconstitute existing vocabularies like EntrezGene or SwissProt.  The file consists of two parts:

* Header

  * Contains Namespace section that captures the namespace definition and where it applies.
  * Contains Author section capturing who is responsible for creating the namespace.
  * Contains Citation section that captures the external vocabulary this namespace is based on.
  * Contains Process section that determines how the namespace file is processed by the OpenBEL Framework.
* Values

  * The Values section contains all biological entities in value/encoding pairs.  The value and encoding is delimited by the namespace's DelimiterString.  Each pair is separated by a newline.
  * Entity encodings allow you to enforce BEL function semantics on a per-entity basis.  Jump to :ref:`my-understanding-entity-encoding`.
  * Format

    * [ENTITY][DelimiterString][ENCODING]
    * Examples

      * AKT1|GRP
      * Abdominal Pain|O
      * difenoxin hydrochloride|A
    * Supports UTF-8 encoded entities.

.. _my-file-format:

File Format
-----------

The following is the structure of a namespace file::

   [Namespace]
   Keyword=KEYWORD (aka regex: \w+)
   NameString=STRING
   DomainString=STRING
   SpeciesString=STRING
   DescriptionString=STRING
   VersionString=STRING
   CreatedDateTime=ISO8601 DATE/TIME
   QueryValueURL=URL

   [Author]
   NameString=STRING
   CopyrightString=STRING
   ContactInfoString=STRING

   [Citation]
   NameString=STRING
   DescriptionString=STRING
   PublishedVersionString=STRING
   PublishedDate=ISO8601 DATE
   ReferenceURL=URL

   [Processing]
   CaseSensitiveFlag=no|yes
   DelimiterString=STRING
   CacheableFlag=yes

   [Values]
   # Single-line comment
   {[ENTITY][DelimiterString][ENCODING]

Each field name ends with the type information for it.

* KEYWORD

  * Represents a string containing `Word Characters`_ only, max length of 8.
  * Preferred namespace prefix when used in BEL language.
* String

  * Represents a user-defined, UTF-8 encoded string (represented as a Java string).
* Date

  * Represents a date in `ISO 8601`_ format.
* DateTime

  * Represents a date/time in `ISO 8601`_ format.
* URL

  * Represents a valid parsable URL.
* Flag

  * Represents a boolean using the values "no" (false) and "yes" (true).

.. _my-field-format:

Field Format
------------

===========  ===========================  ===========================================================================  =========
Block        Field                        Description                                                                  Required
===========  ===========================  ===========================================================================  =========
Namespace    Keyword                      Preferred BEL Keyword, `Word Characters`_, max length of 8                   Yes
Namespace    NameString                   Namespace name, UTF-8 encoded string                                         Yes
Namespace    DomainString                 One of : "BiologicalProcess", "Chemical", "Gene and Gene Products", "Other"  Yes
Namespace    SpeciesString                Comma-separated list of `species taxonomy ids`_                              No
Namespace    DescriptionString            Namespace description, UTF-8 encoded string                                  No
Namespace    VersionString                Namespace version, UTF-8 encoded string                                      No
Namespace    CreatedDateTime              Namespace publish timestamp, `ISO 8601`_ Date/Time                           Yes
Namespace    QueryValueURL                HTTP URL to query for details on namespace values (must be valid URL)        No
Author       NameString                   Namespace's authors, UTF-8 encoded string                                    Yes
Author       CopyrightString              Namespace's copyright/license information, UTF-8 encoded string              No
Author       ContactInfoString            Namespace author's contact info, UTF-8 encoded string                        No
Citation     NameString                   Citation name, UTF-8 encoded string                                          Yes
Citation     DescriptionString            Citation description, UTF-8 encoded string                                   No
Citation     PublishedVersionString       Citation version, UTF-8 encoded string                                       No
Citation     PublishedDate                Citation publish timestamp, `ISO 8601`_ Date                                 No
Citation     ReferenceURL                 URL to more citation information (must be valid URL)                         No
Processing   CaseSensistiveFlag (unused)  no for case-insensitive lookup, yes for case-sensitive lookup                No
Processing   DelimiterString              User-defined delimiter string that splits namespace value from encoding      Yes
Processing   CacheableFlag (unused)       no to never cache namespace, yes to always cache                             No
===========  ===========================  ===========================================================================  =========

.. _my-understanding-entity-encoding:

Understanding Entity Encoding
-----------------------------

The entity encoding allows the OpenBEL Framework to enforce functional semantics when processing BEL documents.  The biological entities can
define a set of encoding flags that indicate which functions apply to this entity.  For example whether an entity produces a protein or not.

The valid encoding values are:

==============  ==================================
Encoding Value  Valid BEL Functions
==============  ==================================
B               bp(), path()
O               path()
R               r(), m()
M               m()
P               p()
G               g()
A               a(), r(), m(), p(), g(), complex()
C               complex()
==============  ==================================

An example would be the HGNC Gene Symbol `GK4P`_.  It can code for a gene and rna abundance, but not a protein.  To capture these semantics
we would add the `GK4P`_ biological entity to the namespace like::

   GK4P|GR


.. _my-quick-start-steps:

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
