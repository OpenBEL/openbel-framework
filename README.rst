OpenBEL Framework
=================

The OpenBEL Framework is an open-platform technology for managing, publishing, and using biological knowledge represented using the Biological Expression Language (BEL). BEL is an easy to understand, computable format for representing biological knowledge. The OpenBEL Framework is specifically designed to
overcome many of the challenges associated with capturing, integrating, and
storing biological knowledge within an organization, and sharing the knowledge across the
organization and between business partners. The framework provides mechanisms
for: 

 #. Capture and management of biological knowledge
 #. Integration of knowledge from multiple sources
 #. Knowledge representation in a standard and open format
 #. Creation of custom, computable biological networks from captured
    knowledge
 #. Enabling applications to query knowledge networks using web and Java APIs

Central to the design of the framework is the ability to integrate knowledge
across different representational vocabularies and ontologies. This allows organizations to combine
knowledge from disparate sources into centralized
knowledge repositories. The combined knowledge can be made available to a
variety of decision support and analytical applications through a standardized
set of computable networks and APIs.

.. contents::

**For a more complete guide to using the BEL Framework, we encourage you to check out our** `wiki pages`_.

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
  repositories of the OpenBEL Workbench.

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
.. _wiki pages: http://wiki.openbel.org

Distribution
------------

The OpenBEL Framework is distributed as an archive that can be extracted and
used on many platforms.

Building
--------

The OpenBEL Framework is built using `Apache Maven`_.

    mvn -Pdistribution clean package assembly:assembly install

Alternatively, you can use ``package.sh`` which does the same thing.

.. _Apache Maven: http://maven.apache.org/

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

**For Users:**
 * Ohloh: https://www.ohloh.net/p/openbel-framework
 * BEL Portal: http://openbel.org
 * Selventa: http://www.selventa.com/
 * API documentation: http://openbel.github.com/openbel-framework
 * Freecode: http://freecode.com/projects/openbel-framework

**For Developers:**
 * API documentation: http://openbel.github.com/openbel-framework
 * User documentation: http://openbel-framework.readthedocs.org/en/master/
 * Nightly builds: http://ci.selventa.com/browse/FWMASTER-NIGHTLY/latest/artifact
 * OpenBEL build server: http://ci.selventa.com
 * GitHub: https://github.com/OpenBEL
 * Freecode: http://freecode.com/projects/openbel-framework
