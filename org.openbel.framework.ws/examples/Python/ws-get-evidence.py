#!/usr/bin/env python
from ws import *
from suds import *
from random import choice

if __name__ == '__main__':
    ws = start()

    kam = choice(ws.service.GetCatalog());
    hndl = ws.service.LoadKam(kam)

    rtypefltrcrit = ws.create('RelationshipTypeFilterCriteria')
    rtypefltrcrit.valueSet = [ 'INCREASES', 'DECREASES' ]
    edgefltr = ws.create('EdgeFilter')
    edgefltr.relationshipCriteria = rtypefltrcrit
    
    edges = ws.service.FindKamEdges(hndl, edgefltr)
    kamfltr = ws.create('KamFilter')
    kamfltr.relationshipCriteria = rtypefltrcrit
    print ws.service.GetSupportingEvidence(choice(edges), kamfltr)

    exit_success()

