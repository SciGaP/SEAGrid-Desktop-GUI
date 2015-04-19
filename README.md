<h1>GridChem-Client Maven version</h1>


<h5>To build the project</h5>

mvn clean install

<h5>To run GridChem-Client in local mode</h5>

./run.sh

<h5>To build Java Webstart (JNLP) pack</h5>

mvn clean install -Pjnlp

* Files are created in target/jnlp folder
* To change configurations of jnlp build, refer to the profile "jnlp" in pom.xml
* Make sure caching temporary files option in Java Control Panel is checked before running launch.jnlp


<h5>Tested systems</h5>

* Mac OS X 10.10.2
* Java version 1.7.0_21

<h3>Configuring WSO2 Identity Server</h3>

* Download WSO2 IS 5.0.0 and Service Pack http://wso2.com/products/identity-server/
* Extract wso2is-5.0.0.zip zip and WSO2-IS-5.0.0-SP01.zip
* Install service pack according to given instructions and start WS02 IS
* Login to admin console https://localhost:9443
* Go to  Home > Identity > Service Providers > Add
* Create a new Service Provider
* Inbound Authentication Configuation > OAuth/OpenID Connect Configuration
* Create an OAuth 2.0 application
* Copy OAuth Client Key and Secret
* Update Gridchem_Home/src/main/resources/airavata.properties file



