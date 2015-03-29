<h1>GridChem-Client Maven version</h1>


<h5>To build the project</h5>

mvn clean install

<h5>To run GridChem-Client in local mode</h5>

./run.sh

<h5>To build Java Webstart (JNLP) pack

mvn clean install -Pjnlp

* Files are reated in target/jnlp folder
* To change configurations of jnlp build, refer to the profile "jnlp" in pom.xml