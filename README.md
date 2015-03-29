<h1>GridChem-Client Maven version</h1>


<h5>To build the project</h5>

mvn clean install

<h5>To run GridChem-Client in local mode</h5>

./run.sh

<h5>To build Java Webstart (JNLP) pack

mvn clean install -Pjnlp

* Files are created in target/jnlp folder
* To change configurations of jnlp build, refer to the profile "jnlp" in pom.xml
* Make sure caching temporary files option in Java Control Panel is checked before running launch.jnlp


<h5>Tested systems</h5>

Mac OS X 10.10.2
Java version 1.7.0_21


