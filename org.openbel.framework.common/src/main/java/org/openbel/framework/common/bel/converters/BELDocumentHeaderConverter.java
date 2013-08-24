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
package org.openbel.framework.common.bel.converters;

import java.util.Arrays;
import java.util.List;

import org.openbel.bel.model.BELDocumentHeader;
import org.openbel.framework.common.model.Header;

public class BELDocumentHeaderConverter extends
        BELConverter<BELDocumentHeader, Header> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Header convert(BELDocumentHeader bdh) {
        String name = bdh.getName();
        String desc = bdh.getDescription();
        String version = bdh.getVersion();
        Header h = new Header(name, desc, version);
        h.setAuthors(Arrays.asList(bdh.getAuthor()));
        h.setContactInfo(bdh.getContactInfo());
        h.setCopyright(bdh.getCopyright());
        h.setDisclaimer(bdh.getDisclaimer());
        h.setLicenses(bdh.getLicense() == null ? null : Arrays.asList(bdh
                .getLicense()));

        return h;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELDocumentHeader convert(Header h) {
        String name = h.getName();
        String author = join(h.getAuthors());
        String copyright = h.getCopyright();
        String contactInfo = h.getContactInfo();
        String description = h.getDescription();
        String disclaimer = h.getDisclaimer();
        String license = join(h.getLicenses());
        String version = h.getVersion();

        return new BELDocumentHeader(name, description, version, author,
                copyright,
                contactInfo,
                disclaimer, license);
    }

    private String join(List<String> strings) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);

            if (i != 0) {
                b.append(", ");
            }

            b.append(s);
        }
        return b.toString();
    }
}
