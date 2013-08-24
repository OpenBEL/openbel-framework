OpenBEL Framework
=================

**For a more complete guide to using the BEL Framework, we encourage you to
check out the** `OpenBEL Wiki`_.

The OpenBEL Framework is an open-platform technology for managing, publishing,
and using biological knowledge represented using the Biological Expression
Language (BEL). BEL is an easy to understand, computable format for representing
biological knowledge. The OpenBEL Framework is specifically designed to
overcome many of the challenges associated with capturing, integrating, and
storing biological knowledge within an organization, and sharing the knowledge
across the organization and between business partners. The framework provides
mechanisms for:

 #. Capture and management of biological knowledge
 #. Integration of knowledge from multiple sources
 #. Knowledge representation in a standard and open format
 #. Creation of custom, computable biological networks from captured
    knowledge
 #. Enabling applications to query knowledge networks using web and Java APIs

Central to the design of the framework is the ability to integrate knowledge
across different representational vocabularies and ontologies. This allows
organizations to combine knowledge from disparate sources into centralized
knowledge repositories. The combined knowledge can be made available to a
variety of decision support and analytical applications through a standardized
set of computable networks and APIs.

.. contents::

The OpenBEL Ecosystem
---------------------

Various smaller projects are connected with the framework. Each project is
intended on serving a particular purpose.

`OpenBEL Discussion Group`_
  This group is used to discuss OpenBEL technologies, the BEL language, and
  anything relevant to the OpenBEL ecosystem as a whole.

  You can subscribe to these announcements by visiting the link, or by sending
  an email to ``openbel-discuss+subscribe@googlegroups.com`` with the subject
  ``subscribe``.

`OpenBEL Announcement Group`_
  This group is used to issue announcements related to OpenBEL.

  You can subscribe to these announcements by visting the link, or by sending an
  email to ``openbel-announce+subscribe@googlegroups.com`` with the subject
  ``subscribe``.

`freenode IRC network`_
  We maintain the ``#openbel`` channel on the freenode network.

  To connect to freenode, you can use ``chat.freenode.net`` as an IRC server.
  On Linux and Windows, you can use `XChat`_ as an IRC client. On Mac OS X,
  there is `Xirc`_.

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
  repositories of the BEL Editor.

`BEL Editor`_
  The BEL Editor enables the BEL language and the framework to be used in
  `Eclipse`_ - from the syntax and semantics of the BEL language to the
  compilation of BEL knowledge into knowledge assembly models.

.. _OpenBEL Discussion Group: https://groups.google.com/forum/#!forum/openbel-discuss
.. _OpenBEL Announcement Group: https://groups.google.com/forum/#!forum/openbel-announce
.. _openbel-framework-resources: https://github.com/OpenBEL/openbel-framework-resources
.. _openbel-framework-examples: https://github.com/OpenBEL/openbel-framework-examples
.. _Cytoscape Plugins: https://github.com/OpenBEL/Cytoscape-Plugins#readme
.. _OpenBEL Eclipse Repository: https://github.com/OpenBEL/eclipse
.. _BEL Editor: https://github.com/OpenBEL/bel-editor
.. _Cytoscape: http://www.cytoscape.org/
.. _Eclipse: http://eclipse.org
.. _freenode IRC network: http://www.freenode.net/
.. _XChat: http://xchat.org/
.. _Xirc: http://www.aquaticx.com/
.. _OpenBEL Wiki: http://wiki.openbel.org

Distribution
------------

The OpenBEL Framework is distributed as an archive that can be extracted and
used on many platforms. See downloads_ for the latest release archive.

.. _downloads: http://download.openbel.org/index.html

Building
--------

The OpenBEL Framework is built using `Apache Maven`_.

    mvn -Pdistribution clean package assembly:assembly install

Alternatively, you can use ``package.sh`` which does the same thing.

.. _Apache Maven: http://maven.apache.org/

Nightly Builds
^^^^^^^^^^^^^^

Builds of the master and experimental branches are made available each day
after rigorous testing.

`master`_
  The master branch is the latest stable version of the framework that has yet
  to be released.

`experimental`_
  The experimental branch is the latest unstable version of the framework
  containing experimental features, improvements, and API changes.

.. _master: http://build.openbel.org/browse/FWMASTER-NIGHTLY/latest/artifact
.. _experimental: http://build.openbel.org/browse/FWEXP-NIGHTLY/latest/artifact

API Documentation
-----------------

Pushes to the `master` branch instruct our build server to update the
`API documentation`_. It will always be up-to-date for the current
master. The documentation is published under the `gh-pages`_ branch of the
official repository.

.. _API documentation: http://openbel.github.com/openbel-framework
.. _gh-pages: https://github.com/OpenBEL/openbel-framework/tree/gh-pages

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

 * BEL Portal: http://openbel.org
 * OpenBEL Wiki: http://wiki.openbel.org
 * Mailing list: https://groups.google.com/forum/#!forum/openbel-discuss
 * Selventa: http://www.selventa.com/
 * API documentation: http://openbel.github.com/openbel-framework
 * User documentation: http://openbel-framework.readthedocs.org/en/master/
 * Ohloh: https://www.ohloh.net/p/openbel-framework
 * Freecode: http://freecode.com/projects/openbel-framework
 * OpenBEL build server: http://build.openbel.org
 * GitHub: https://github.com/OpenBEL

