package demo.mtom.streamreceiving.server;

import java.io.IOException;

import java.io.InputStream;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.MTOM;

import demo.mtom.streamsending.client.Driver;
import demo.mtom.streamsending.client.StateAsSeenByServer;

/**
* <h4>Copyright (C) 2012 innoQ Deutschland GmbH</h4>
* 
* <p>Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* <p>You may obtain a copy of the License at
* <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a>
* <p>Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* 
* @author Andreas Kr&uuml;ger
*/
@WebService(name="Blobconsumer")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@MTOM(enabled=true)
public class WSBean {

    @WebMethod
    public String echo(String input) {
        return "I repeat: \"" + input + "\".";
    }

    @WebMethod
    public String feedMe(BigDataRequest request) throws IOException {
        InputStream stream1 = request.bigblob1.getInputStream();
        long bigblob1Length = readData(stream1, StateAsSeenByServer.RECEIVING_BLOB_1);
        InputStream stream2 = request.bigblob2.getInputStream();
        long bigblob2Length = readData(stream2, StateAsSeenByServer.RECEIVING_BLOG_2);
        String result = request.name + " read data blob1 " + bigblob1Length + ", blob2 " + bigblob2Length;
        return result;
    }

    private long readData(InputStream stream, StateAsSeenByServer state) throws IOException {
        long result = 0;
        byte buffer[] = new byte[3140];
        boolean first = true;
        for(int readNow = stream.read(buffer); -1 != readNow; readNow = stream.read()) {
            if(first) {
                first = false;
                Driver.stateAsSeenByServer = state;
            }
            result += readNow;
        }
        return result;
    }
}
