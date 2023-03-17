mvn clean install

mv /home/gangani/Desktop/eco-system/IDV/identity-verification/components/org.wso2.carbon.extension.identity.verifier/target/org.wso2.carbon.extension.identity.verifier-1.0.0-SNAPSHOT.jar /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/components/dropins
mv /home/gangani/Desktop/eco-system/IDV/identity-verification/components/org.wso2.carbon.extension.identity.verification.provider/target/org.wso2.carbon.extension.identity.verification.provider-1.0.0-SNAPSHOT.jar /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/components/dropins
mv /home/gangani/Desktop/eco-system/IDV/identity-verification/components/org.wso2.carbon.extension.identity.verification.claim.mgt/target/org.wso2.carbon.extension.identity.verification.claim.mgt-1.0.0-SNAPSHOT.jar /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/components/dropins
mv /home/gangani/Desktop/eco-system/IDV/identity-verification/components/org.wso2.carbon.extension.identity.verifier.Onfido/target/org.wso2.carbon.extension.identity.verifier.Onfido-1.0.0-SNAPSHOT.jar /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/components/dropins
rm -rf /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/deployment/server/webapps/api#idv.war
rm -rf /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/deployment/server/webapps/api#idv
mv /home/gangani/Desktop/eco-system/IDV/identity-verification/components/org.wso2.carbon.extension.identity.verification.api/org.wso2.carbon.extension.identity.verification.api.dispatcher/target/api#idv.war /home/gangani/Desktop/eco-system/IS/new-0/wso2is-6.2.0-m1-SNAPSHOT/repository/deployment/server/webapps
