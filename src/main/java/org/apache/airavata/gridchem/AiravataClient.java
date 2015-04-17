package org.apache.airavata.gridchem;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.auth.OAuthClient;
import org.apache.thrift.protocol.TProtocol;

/**
 * Created by dimuthuupeksha on 4/16/15.
 */
public class AiravataClient extends Airavata.Client{

    public AiravataClient(TProtocol prot) {
        super(prot);
    }

    public AiravataClient(TProtocol iprot, TProtocol oprot) {
        super(iprot, oprot);
    }

    public OAuthClient getOAuthProvider(String endPoint,String clientId, String clientSecret){
        return new OAuthClient(endPoint,clientId,clientSecret);
    }

}
