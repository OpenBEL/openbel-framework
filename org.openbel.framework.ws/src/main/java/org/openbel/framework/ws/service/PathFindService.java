/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.ws.service;

import java.util.List;

import org.openbel.framework.api.Kam;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.SimplePath;

public interface PathFindService {

    public List<SimplePath> findPaths(final Kam kam,
            final List<KamNode> sources,
            final List<KamNode> targets,
            final int maxSearchDepth) throws PathFindServiceException;

    public List<SimplePath> scan(final Kam kam,
            final List<KamNode> sources,
            final int maxSearchDepth)
            throws PathFindServiceException;

    public List<SimplePath> interconnect(final Kam kam,
            final List<KamNode> sources, final int maxSearchDepth)
            throws PathFindServiceException;
}
