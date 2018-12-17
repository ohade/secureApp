keytool -genkeypair -alias akey -keyalg RSA -keysize 2048 -dname "CN=Side A, OU=Building secured apps, O=Building secured apps, L=Israel, ST=Unknown, C=IL"  -keypass "ASecPass@#" -validity 100 -storetype JKS -keystore akeystore.jks -storepass "AStPass()"


keytool -genkeypair -alias bkey -keyalg RSA -keysize 2048 -dname "CN=Side B, OU=Building secured apps, O=Building secured apps, L=Israel, ST=Unknown, C=IL" -keypass "BSecPass$%" -validity 100 -storetype JKS -keystore bkeystore.jks -storepass "BSt[]Pass"

# export:
keytool -exportcert -alias akey -keypass "ASecPass@#" -storetype JKS -keystore akeystore.jks -file acert.cert -rfc -storepass "AStPass()"

keytool -exportcert -alias bkey -keypass "BSecPass$%" -storetype JKS -keystore bkeystore.jks -file bcert.cert -rfc -storepass "BSt[]Pass"

# import:
keytool -importcert -alias bkeyremote -keypass "BAnotherSecPass$%" -storetype JKS -keystore akeystore.jks -file bcert.cert -rfc -storepass "AStPass()"

keytool -importcert -alias akeyremote -keypass "AAnotherSecPass@#" -storetype JKS -keystore bkeystore.jks -file acert.cert -rfc -storepass "BSt[]Pass"

