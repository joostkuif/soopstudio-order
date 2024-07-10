# soopstudio-order

## What it is
A small and simple example of a camel (java based) integration that polls a configurable folder in an imap mailbox. If a new unread mail (by a serverside rule) is encountered in this folder a configurable homeassistant automation will be triggered.

## Why did i build it
My wife recently started a business producing and selling soap. When a order is done via her webshop i wanted to notify her, me and the kids of the new order :-)

I wanted to do this in a container and running it as a native compiled executable to minimize the load and memory usage on my Synology NAS, or be able to move it to AWS or Azure Container Instance later. A nice thing from those containers is that they should be quite easy be able to be deployed on other container environments like the mentioned ones before or on Kubernetes or Openshift. The image is therefore publicly available on docker hub.

## Configuration
To run this integration locally in your IDE you can edit the [application.properties](src/main/resources/application.properties)

### from
- imap.server: The servername (and portnumber if non standard) of your imap server
- imap.username: The username where you login with
- imap.password: The password, in my case with a google account i needed to enable multi factor authentication and create an specific app password
- imap.pollfolder: The folder to poll, when a new unread message is found the integration will be triggered. this email wil get status read afterwards.

### to
- homeassistant.server: The url with portnumer where the camel interface can connect to your homeassistant instance http://homeassistant.local:8123 If you run into DNS issues like me, use the IP adress of your HA instance.
- homeassistant.bearer: In Homeassistant, to be able to autenticate/authorize to the api you have to create a long lived token and put tha value here
- homeassistant.automation: In Homeassitant, under settings->automations you can create any automation, here you configure the name of your automation to be run.

## Containerized
You need to install [docker](https://docker.io). the native compilation into a linux executable will be done in a linux container too.

When you run the software as a container, all above values will have to be defined as environment variables inside the container:
- imap.server -> IMAP_SERVER
- imap.username -> IMAP_USERNAME
- etc.

There is an example script [buildcontainer.sh](buildcontainer.sh) that i use to build, tag and push the fresh container into docker hub.
The script pushes towards https://hub.docker.com/repository/docker/jkuif/soopstudio-order


## Based on Quarkus
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/soopstudio-order-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Camel Core ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Simple and Tokenize
- Camel Mail ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/mail.html)): Send and receive emails using imap, pop3 and smtp protocols
