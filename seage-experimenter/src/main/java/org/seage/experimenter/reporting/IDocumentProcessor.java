package org.seage.experimenter.reporting;

import org.w3c.dom.Document;

public interface IDocumentProcessor
{
    boolean isInvolved(Document doc);

    void processDocument(Document doc, String containerFileName) throws Exception;

}
