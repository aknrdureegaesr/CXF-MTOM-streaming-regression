
package demo.mtom.streamsending.client.stubs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the demo.mtom.streamsending.client.stubs package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Echo_QNAME = new QName("http://server.streamreceiving.mtom.demo/", "echo");
    private final static QName _EchoResponse_QNAME = new QName("http://server.streamreceiving.mtom.demo/", "echoResponse");
    private final static QName _FeedMeResponse_QNAME = new QName("http://server.streamreceiving.mtom.demo/", "feedMeResponse");
    private final static QName _IOException_QNAME = new QName("http://server.streamreceiving.mtom.demo/", "IOException");
    private final static QName _FeedMe_QNAME = new QName("http://server.streamreceiving.mtom.demo/", "feedMe");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: demo.mtom.streamsending.client.stubs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FeedMeResponse }
     * 
     */
    public FeedMeResponse createFeedMeResponse() {
        return new FeedMeResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link FeedMe }
     * 
     */
    public FeedMe createFeedMe() {
        return new FeedMe();
    }

    /**
     * Create an instance of {@link EchoResponse }
     * 
     */
    public EchoResponse createEchoResponse() {
        return new EchoResponse();
    }

    /**
     * Create an instance of {@link Echo }
     * 
     */
    public Echo createEcho() {
        return new Echo();
    }

    /**
     * Create an instance of {@link BigDataRequest }
     * 
     */
    public BigDataRequest createBigDataRequest() {
        return new BigDataRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Echo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.streamreceiving.mtom.demo/", name = "echo")
    public JAXBElement<Echo> createEcho(Echo value) {
        return new JAXBElement<Echo>(_Echo_QNAME, Echo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EchoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.streamreceiving.mtom.demo/", name = "echoResponse")
    public JAXBElement<EchoResponse> createEchoResponse(EchoResponse value) {
        return new JAXBElement<EchoResponse>(_EchoResponse_QNAME, EchoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FeedMeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.streamreceiving.mtom.demo/", name = "feedMeResponse")
    public JAXBElement<FeedMeResponse> createFeedMeResponse(FeedMeResponse value) {
        return new JAXBElement<FeedMeResponse>(_FeedMeResponse_QNAME, FeedMeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.streamreceiving.mtom.demo/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FeedMe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.streamreceiving.mtom.demo/", name = "feedMe")
    public JAXBElement<FeedMe> createFeedMe(FeedMe value) {
        return new JAXBElement<FeedMe>(_FeedMe_QNAME, FeedMe.class, null, value);
    }

}
