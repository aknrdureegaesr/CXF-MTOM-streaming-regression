This demonstrates a regression in MTOM streaming in Apache CXF, recently fixed.

## What you run

You run `demo.mtom.streamsending.client.Driver` as a JUnit 4 test.

This test combines a JAX-WS client and server. It starts the server in the background, the test exercises the client.

The following instructions assume you use Eclipse to do so, though that is not really essential.

You can run this in three configurations. Common in all configurations is the use of JDK 6.  Personally, I used this precise version:

    $ java -version
    java version "1.6.0_24"
    OpenJDK Runtime Environment (IcedTea6 1.11.1) (ArchLinux-6.b24_1.11.1-3-x86_64)
    OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)


### Plain JDK 6 configuration

* (Remove cxf-manifest.jar from .classpath if there.)
* run demo.mtom.streamsending.client.Driver as a JUnit test.

This uses the reference implementation (RI) that's part of JDK 6. The RI does not do streaming. So expected outcome: testTwoAttachmentsRequiringStreaming fails (the other three pass).


### CXF version 2.3.1

* Unpack apache-cxf-2.3.1 binary distribution,
* add apache-cxf-2.3.1/lib/cxf-manifest.jar to the classpath,
* run demo.mtom.streamsending.client.Driver as a JUnit test.

CXF knows how to do streaming.

Expected outcome and outcome seen by me: All tests pass.


### CXF version 2.6.0

* Unpack apache-cxf-2.6.0 binary distribution
* add apache-cxf-2.6.0/lib/cxf-manifest.jar to the classpath,
* run demo.mtom.streamsending.client.Driver as a JUnit test.

Exected outcome: Same as 2.3.1.

_Outcome seen: Same as plain JDK 6._

This is a regression, CXF 2.6.0 no longer does MTOM streaming properly.

### CXF version 2.6.1

* Fixed according to [https://issues.apache.org/jira/browse/CXF-4298]
* TODO: Retest.

(Apache fixes considerably faster than I can find the time to retest! Wow!)

### For the record

I generated the WSDL in directory src via

    cd src
    ../../apache-cxf-2.3.1/bin/java2ws -frontend jaxws -wsdl -d WEB-INF -cp ../bin demo.mtom.streamreceiving.server.WSBean
 
Then, the client stubs were generated with

    ../../apache-cxf-2.3.1/bin/wsdl2java -p demo.mtom.streamsending.client.stubs -d . -client -validate WEB-INF/WSBeanService.wsdl

I had to remove three WSBeanService constructors that did not compile (at least not with plain JDK), for lack of Service super-constructors.



#### Copyright (C) 2012 innoQ Deutschland GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Author: Andreas Kr&uuml;ger
