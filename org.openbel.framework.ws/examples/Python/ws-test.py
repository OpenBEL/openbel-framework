#!/usr/bin/env python
from ws import *
from suds import *

if __name__ == '__main__':
    ws = start()
    try:
        ws.service.GetCatalog()
    except WebFault:
        print 'FAILED!'
        print sys.exc_info()[1]
        exit_failure()

    print 'Success!'
    exit_success()

