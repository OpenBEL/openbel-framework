import sys

print
print 'OpenBEL Framework V1.1.1: Web API Client, KAM Lister Demo'
print 'Copyright (c) 2011-2012, Selventa. All Rights Reserved'
print

try:
    from suds.client import Client
except:
    print 'Suds SOAP library not found.'
    print '(https://fedorahosted.org/suds/)'
    sys.exit(1)


url = 'http://localhost:8080/openbel-ws/wsdl/belframework.wsdl'
client = Client(url)

catalog = client.service.GetCatalog()

print '   === LISTING KAMs ===   '
for entry in catalog:
    print '   ' + entry.name
print

for entry in catalog:
    name = entry.name
    kam = client.factory.create('Kam')
    kam.name = name
    print '   === LOADING KAM ===   '
    print '   KAM: "' + name + '"'
    hndl = client.service.LoadKam(kam)
    print '   KAM loaded'
    print
    documents = client.service.GetBelDocuments(hndl)
    print '   === LISTING DOCUMENTS ===   '
    for document in documents:
        id = document.id
        name = document.name
        desc = document.description
        print '   Name:', name
        print '   Description:', desc
        print '   ID:', id
        print
    print

print 'KAM Lister Demo finished.'
sys.exit(0)
