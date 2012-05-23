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

.. contents::

The OpenBEL Ecosystem
---------------------

Various smaller projects are connected with the framework. Each project is
intended on serving a particular purpose.

`openbel-framework-resources`_
  The framework's resources provide a set of files derived from biological and
  chemical ontologies. These resources are used to better describe biological
  statements and their context.

`openbel-framework-examples`_
  This repository is for helping developers get started with different uses of
  the framework. It contains a number of examples in various programming
  languages.

`Cytoscape Plugins`_
  The `Cytoscape`_ plugins enable Cytoscape to access and manipulate the OpenBEL
  Framework knowledge assembly models using the framework's web service API.

`OpenBEL Eclipse Repository`_
  The OpenBEL Eclipse p2 repository. It currently holds the stable and unstable
  repositories of the OpenBEL Workbench.

`OpenBEL Workbench`_
  The OpenBEL Workbench enables the BEL language and the framework to be used
  in `Eclipse`_ - from the syntax and semantics of the BEL language to the
  compilation of BEL knowledge into knowledge assembly models.

.. _openbel-framework-resources: https://github.com/OpenBEL/openbel-framework-resources
.. _openbel-framework-examples: https://github.com/OpenBEL/openbel-framework-examples
.. _Cytoscape Plugins: https://github.com/belframework-org/Cytoscape-Plugins#readme
.. _OpenBEL Eclipse Repository: https://github.com/belframework-org/eclipse
.. _OpenBEL Workbench: https://github.com/belframework-org/OpenBEL-Workbench
.. _Cytoscape: http://www.cytoscape.org/
.. _Eclipse: http://eclipse.org

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

API Documentation
-----------------

Pushes to the `master` branch instruct our build server to update the
`API documentation`_. It will always be up-to-date for the current
master. The documentation is published under the `gh-pages`_ branch of this
repository.

.. _API documentation: http://openbel.github.com/openbel-framework
.. _gh-pages: https://github.com/OpenBEL/openbel-framework/tree/gh-pages

License
-------

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The OpenBEL Framework is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.

Additional Terms under LGPL v3:

This license does not authorize you and you are prohibited from using the
name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
or, in the discretion of other licensors or authors of the program, the
name, trademarks, service marks, logos or similar indicia of such authors or
licensors, in any marketing or advertising materials relating to your
distribution of the program or any covered product. This restriction does
not waive or limit your obligation to keep intact all copyright notices set
forth in the program as delivered to you.

If you distribute the program in whole or in part, or any modified version
of the program, and you assume contractual liability to the recipient with
respect to the program or modified version, then you will indemnify the
authors and licensors of the program for any liabilities that these
contractual assumptions directly impose on those licensors and authors.

Further Reference
-----------------

 * API documentation: http://openbel.github.com/openbel-framework
 * GitHub: https://github.com/belframework-org
 * BEL Portal: http://belframework.org/
 * Selventa: http://www.selventa.com/
 * Cytoscape Plugins: https://github.com/belframework-org/Cytoscape-Plugins
 * Eclipse integration: https://github.com/belframework-org/OpenBEL-Workbench
 * OpenBEL Eclipse repository: https://github.com/belframework-org/eclipse
 * OpenBEL build server: http://ci.selventa.com
 * Ohloh: https://www.ohloh.net/p/openbel-framework
 * Freecode: http://freecode.com/projects/openbel-framework

