package demo.mtom.streamsending.client;

import static demo.mtom.streamsending.client.stubs.WSBeanService.WSDL_LOCATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import demo.mtom.streamreceiving.server.Server;
import demo.mtom.streamsending.client.stubs.BigDataRequest;
import demo.mtom.streamsending.client.stubs.Blobconsumer;
import demo.mtom.streamsending.client.stubs.WSBeanService;

/**
 * JUnit test to establish whether WS-MTOM-attachments are streamed.
 *
 * <p>You are advised to run this test in a JVM by itself.
 * 
 * <h4>Our definition of MTOM-<em>streaming</em></h4>
 * 
 * <p>The entire MTOM-attachment is never stored in total, but (disregarding
 * a few kByte network buffers) is consumed by the WS-server while being produced
 * by the WS-client.</p>
 * 
 * <h4>How does this test function</h4>
 * 
 * <p>This JUnit test sets up a WS-server in the background, the
 * JUnit test methods run the client.
 * 
 * <p>There are a few unimportant tests that basically just check that
 * client and server are functional to begin with. So, irregardless of
 * MTOM streaming or not, these tests will always come out ok:
 * 
 * <ul>
 * <li>{@link #testStupidEchoService()}
 * <li>{@link #testStupidEchoService2()}
 * <li>{@link #testTwoSmallAttachments()}
 * </ul>
 * 
 * <p>If any of these fails, your setup is not as intended by
 * this test's original author.</p>
 * 
 * <p>The real test is {@link #testTwoAttachmentsRequiringStreaming()}.
 * Here is a description what it does:
 * 
 * <p>The client will send two MTOM attachments in its request. The server's
 * {@link demo.mtom.streamreceiving.server.WSBean} will tell the client
 * whether it has started seeing bytes from the first and, later,
 * from the second attachment. (As client and server live in the same JVM,
 * this communication is done by direct write access, from the server,
 * to {@link #stateAsSeenByServer}.)
 * 
 * <p>The client keeps sending more and more attachment bytes as required,
 * until the server starts seeing the start of that attachment.
 * 
 * <p>If there is no streaming, this will never happen. Someone in the middle
 * will be attempting to store the attachment in total, before the server's
 * {@link demo.mtom.streamreceiving.server.WSBean} will ever get to see
 * the first byte. If this happens, the client will generate more and more
 * attachment material until, finally, after 
 * {@link #DONT_THINK_WE_HAVE_STREAMING_AFTER_THESE_MANY_BYTES} bytes,
 * an emergency break triggers and the test fails.  So this is what to
 * expect if streaming does not happen for both attachments.
 * 
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
public class Driver {

    private static final int DONT_THINK_WE_HAVE_STREAMING_AFTER_THESE_MANY_BYTES = 10000000;

    private static final QName SERVICE_NAME = new QName("http://server.streamreceiving.mtom.demo/", "WSBeanService");

    
    /** Hints for tracing what is going on between client and server:
    <p>One way is, activate the alternate SERVER_URL to point to a different port, e.g.,</p>
    <pre>
    private static final String SERVER_URL = "http://localhost:9010/BlobconsumerPort";
    </pre>
    <p>The server still services whatever is configured
    via {@link Server#SERVER_URL}, but the client expects these services
    at the new place.
    <p>The trick is to establish some TCP/IP forwarding between those two.
    <p>Do do that, on Linux, I use <code>netcat</code> .
    It allows me to trace what gets sent and received.
    <p>To set this up, run (once)</p>
    <pre>
    mknod pipe p
    </pre>
    <p>Then, for each test,</p>
    <pre>
    tee &lt; pipe response | nc -v -v -l -p 9010 | 
    tee request | nc -v -v localhost 9090 > pipe
    </pre><!-- if you copy from source, replace the "&lt;" by "<" -->
    
    <p>This gives straightforward logging of request and response.
    
    <p>There is one complication, though:
    That command has to be started <em>after</em> the server has started,
    but <em>before</em> the client starts.
    
    <p>For a simple solution, add</p>
    <pre>
    Tread.sleep(10000);
    </pre>
    <p>after the <code>Server()</code> constructor. This gives you time
    to start the command manually.
    
    <p>For normal operation, let the client send the request
    to the URL serviced by the server:</p>
     */
    private static final String SERVER_URL = Server.SERVER_URL;
    
    /**
     * We reuse the same client instance for all tests.
     * <p>This has the advantage that only one HTTP connection is needed.</p>
     */
    private static Blobconsumer port;

    @BeforeClass
    public static void initializeClient() {
        WSBeanService ss = new WSBeanService(WSDL_LOCATION, SERVICE_NAME);
        port = ss.getBlobconsumerPort();

        // Activate MTOM on the client side:
        ((SOAPBinding)((BindingProvider) port).getBinding()).setMTOMEnabled(true);

        // Potentially change where we expect to be serviced:
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SERVER_URL);
        
        // Activate streaming on the client side,
        // so it is not the client that stores the attachments before sending.
        // In the author's experience, this always works, irregardless of WS stack used:
        @SuppressWarnings("restriction")
        // If you see an error in Eclipse here:
        // Navigate Window / Preferences / Java / Compiler/ Errors/Warnings /
        // Deprecated and restricted API / Forbidden reference (access rules):
        // and change from "Error" to "Warning".
        String doChunking = com.sun.xml.internal.ws.developer.JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE;
        ((BindingProvider) port).getRequestContext().put(doChunking, 4096);
    }

    @BeforeClass
    public static void startServer() throws Exception {
        new Server();
        System.out.println("Server is running, test may proceed...");
    }

    /**
     * This just tests the little echo service.
     * <p>This is expected to succeed, for any WS implementation.
     */
    @Test
    public void testStupidEchoService() throws Exception {
        String result = port.echo("I want an answer.");
        Assert.assertEquals("I repeat: \"I want an answer.\".", result);
    }

    /**
     * Just to check we can do two invocations on the same client.
     */
    @Test
    public void testStupidEchoService2() throws Exception {
        String result = port.echo("Thanks for talking to me.");
        Assert.assertEquals("I repeat: \"Thanks for talking to me.\".", result);
    }

    @Test
    public void testTwoSmallAttachments() throws Exception {
        BigDataRequest data = new BigDataRequest();
        data.setName("an easy one");
        DataHandler blob1Handler = shortBlobDataHandler("blub1", "Just a little string of mine, let it shine.");
        data.setBigblob1(blob1Handler);
        DataHandler blob2Handler = shortBlobDataHandler("blub2", "This is a somewhat darker string.");
        data.setBigblob2(blob2Handler);
        String answer = port.feedMe(data);
        assertEquals("an easy one read data blob1 43, blob2 33", answer);
    }

    /**
     * Here is the real test, as explained above.
     */
    @Test
    public void testTwoAttachmentsRequiringStreaming() throws Exception {
        stateAsSeenByServer = StateAsSeenByServer.WAITING;
        BigDataRequest data = new BigDataRequest();
        data.setName("this is the tough one");
        DataHandler blob1Handler = waitForStreamingBlobDataHandler("bigblub1", StateAsSeenByServer.RECEIVING_BLOB_1);
        data.setBigblob1(blob1Handler);
        DataHandler blob2Handler = waitForStreamingBlobDataHandler("bigblub2", StateAsSeenByServer.RECEIVING_BLOG_2);
        data.setBigblob2(blob2Handler);
        String answer = port.feedMe(data);
        System.out.println(answer);
    }

    /** Provide a {@link DataHandler} for some short attachment. */
    private DataHandler shortBlobDataHandler(String blobName, String blobContent) throws UnsupportedEncodingException {
        InputStream blobInputStream = new ByteArrayInputStream(blobContent.getBytes("UTF-8"));
        DataHandler blobHandler = handlerFromStream(blobName, blobInputStream);
        return blobHandler;
    }

    /** The bean writes here to tell us it which attachment it sees.*/
    public static volatile StateAsSeenByServer stateAsSeenByServer;

    /** Produce more and more data until the bean starts to see some. */
    private static class ProduceDataUntilServerSeesSome extends InputStream {
        private final StateAsSeenByServer waitForState;
        private int c = 1;
        private int count;
        
        public ProduceDataUntilServerSeesSome(StateAsSeenByServer waitForState) {
            this.waitForState = waitForState;
        }

        /** Not efficient, but straightforward and works. */
        @Override
        public int read() throws IOException {
            int result;
            if(waitForState != stateAsSeenByServer) {
                // As a teenager, I found out
                // that this formula will generate all numbers
                // between 1 and 498 in some odd sequence
                // before repeating. So I use this as some (stupid)
                // random number generator, with fond memories.
                c = c * 10 % 499;

                result = 0xFF & c;
                count ++;
                if(DONT_THINK_WE_HAVE_STREAMING_AFTER_THESE_MANY_BYTES < count)
                    fail("This isn't streaming, sorry.");
            } else {
                result = -1;
            }
            return result;
        }
    }

    /** Wrap a {@link DataHandler} around a {@link ProduceDataUntilServerSeesSome} .*/
    private DataHandler waitForStreamingBlobDataHandler(String blobName,
            StateAsSeenByServer waitForState) {
        InputStream blobInputStream = new ProduceDataUntilServerSeesSome(waitForState);
        DataHandler blobHandler = handlerFromStream(blobName, blobInputStream);
        return blobHandler;
    }

    /** Wrap a {@link DataHandler} around a {@link InputStream}. */
    private DataHandler handlerFromStream(final String blobName,
            final InputStream blobInputStream) {
        DataSource blobSource = new DataSource() {

            @Override
            public InputStream getInputStream() throws IOException {
                return blobInputStream;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                throw new UnsupportedOperationException("Not needed (I thought), so not implemented.");
            }

            @Override
            public String getContentType() {
                return "application/octed-stream";
            }

            @Override
            public String getName() {
                return blobName;
            }
        };
        DataHandler blobHandler = new DataHandler(blobSource);
        return blobHandler;
    }


}
