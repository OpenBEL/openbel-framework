#!/bin/bash

# unit test 1
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_1.bel -k test1 -d test1
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_1.bel -k test1epf -d test1epf --expand-protein-families
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_1.bel -k test1ngs -d test1ngs --no-gene-scaffolding

# unit test 2
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_2.bel -k test2 -d test2
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_2.bel -k test2npf -d test2npf --no-protein-families

# unit test 3
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_3.bel -k test3 -d test3

# unit test 4
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_4.bel -k test4enc -d test4enc --expand-named-complexes
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_4.bel -k test4enc_ngs -d test4enc_ngs --expand-named-complexes --no-gene-scaffolding

# unit test 5
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_5.bel -k test5ngs -d test5ngs --no-gene-scaffolding

# unit test 6
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_6.bel -k test6np3 -d test6np3 --no-phaseIII

# unit test 7
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_7.bel -k test7 -d test7

# unit test 8
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_8.bel -k test8 -d test8

# unit test 9
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_9.bel -k test9 -d test9

# unit test 10
./belc.sh -v -t -f ~/work/selventa_bel/branches/proto_network/docs/belscript/unit_tests/test_10.xbel -k test10 -d test10
