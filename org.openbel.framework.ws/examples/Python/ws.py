try:
    from suds.client import Client
    from suds.wsse import *
except ImportError as ie:
    print
    print "You're missing suds, the lightweight SOAP python client."
    print "(https://fedorahosted.org/suds/)"
    print
    raise ie
import logging

class WS:

    '''
    Creates the WS connection from the URL, username, and password.
    '''
    def __init__(self, wsdl_url, username = None, password = None):
        self.client = Client(wsdl_url)
        cache = self.client.options.cache
        cache.setduration(seconds = 1)

        if username and password:
            token = UsernameToken(username, password)
            token.setnonce()
            token.setcreated()
            security = Security()
            security.tokens.append(token)
            self.client.set_options(wsse = security)
        self.service = self.client.service

    def __str__(self):
        return str(self.client)

    def create(self, obj):
        return self.client.factory.create(obj)

def usage():
    print 'Usage:', me, '<wsdl_url> <username> <password>'
    print "Try '" + me, " --help' for more information."
    
def help():
    print 'Usage:', me, '<wsdl_url> <username> <password>'
    print 'Estalishes a connection to web services.'
    print 'Example:', me, 'http://host:8080/GTPWebServices/webapi/webapi.wsdl [myUserName] [myPassword]'
    print
    print 'Miscellaneous:'
    print '  -h, --help\t\tdisplay this help and exit'
    print
    
def exit_success():
    sys.exit(0)
    
def exit_failure():
    sys.exit(1)

def ws_print(items):
    for i in range(0, len(items)):
        print '#%d' % (i + 1)
        for entry in items[i]:
            name = str(entry[0]).strip('\n')
            value = str(entry[1]).strip('\n')
            print '\t%s: %s' % (name, value)
        print

def start():
    global me, ws
    me = sys.argv[0]

    for arg in sys.argv:
        if arg == '--help' or arg == '-h':
            help()
            exit_failure()        
    
    if len(sys.argv) == 4:
        url, un, pw = sys.argv[1:4]
        ret = WS(url, un, pw)
        logger = logging.getLogger('suds.client')
        logger.setLevel(logging.CRITICAL)
        return ret

    if len(sys.argv) == 2:
        url = sys.argv[1]
        ret = WS(url)
        logger = logging.getLogger('suds.client')
        logger.setLevel(logging.CRITICAL)
        return ret

    me = sys.argv[0]
    usage()
    exit_failure()

