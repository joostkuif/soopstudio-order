import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ImapRouteBuilder extends RouteBuilder {

    @ConfigProperty(name = "imap.server")
    String imapServer;

    @ConfigProperty(name = "imap.username")
    String imapUserName;

    @ConfigProperty(name = "imap.password")
    String imapPassword;

    @ConfigProperty(name = "imap.pollfolder")
    String imapPollFolder;

    @ConfigProperty(name = "homeassistant.server")
    String homeAssistantServer;

    @ConfigProperty(name = "homeassistant.bearer")
    String homeAssistantBearer;

    @ConfigProperty(name = "homeassistant.automation")
    String homeAssistantAutomation;

    private static final String DIRECT_HOMEASSISTANT = "direct:homeassistant";

    @Override
    public void configure() {

        from("imaps://%s?username=%s&password=RAW(%s)&delete=false&unseen=true&folderName=%s".formatted(imapServer, imapUserName, imapPassword, imapPollFolder))
                .routeId("ImapRoute")
                .id("imap-route")
                .to("log:sample.log?showAll=true&multiline=true")
                .to(DIRECT_HOMEASSISTANT)
        ;

        from(DIRECT_HOMEASSISTANT)
                .routeId("HA-RESTApiCallRoute")
                .id("ha-rest-api-call-route")
                .removeHeaders("*")

                .setBody().constant("{\"entity_id\": \"automation.%s\"}".formatted(homeAssistantAutomation))
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Authorization", constant("Bearer %s".formatted(homeAssistantBearer)))
                .to("%s/api/services/automation/trigger?bridgeEndpoint=true".formatted(homeAssistantServer))
                .log("another incoming mail processed")
        ;
    }
}