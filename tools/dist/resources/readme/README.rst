OpenBEL Framework
=================

The OpenBEL Framework is an open-platform technology specifically designed to
overcome many of the challenges associated with capturing, integrating, and
storing knowledge within an organization, and sharing the knowledge across the
organization and between business partners. The framework provides mechanisms
for:

 #. Knowledge capture and management
 #. Integration of knowledge from multiple disparate knowledge streams
 #. Knowledge representation and standardization in an open use-neutral format
 #. Creating customizable and computable biological networks from captured
    knowledge
 #. Quickly enabling knowledge-aware applications using standardized APIs
    across all major platforms

Central to the design of the framework is the ability to integrate knowledge
across different representational vocabularies and ontologies. This is unique
within the industry and for the first time allows organizations to combine
knowledge from disparate sources such as existing applications, internal
sources, and knowledge acquired from business partners, into centralized
knowledge repositories. The combined knowledge can be made available to a
variety of decision support and analytical applications through a standardized
set of computable networks and APIs.

Distribution
------------

The OpenBEL Framework is distributed as an archive that can be extracted and
used on many platforms.

Building
--------

The OpenBEL Framework is built using `Apache Maven`_.

    mvn -Pdistribution clean package assembly:assembly install

Alternatively, you can use `package.sh` which does the same thing.

.. _Apache Maven: http://maven.apache.org/

License
-------

   Copyright 2013 OpenBEL Consortium

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

Further Reference
-----------------

 * GitHub: https://github.com/belframework-org
 * BEL Portal: http://belframework.org/
 * Selventa: http://www.selventa.com/
 * Cytoscape Plugins: https://github.com/belframework-org/Cytoscape-Plugins
 * Eclipse integration: https://github.com/belframework-org/OpenBEL-Workbench
 * OpenBEL Eclipse repository: https://github.com/belframework-org/eclipse
 * OpenBEL build server: http://ci.selventa.com
 * Ohloh:
 * Freecode:
