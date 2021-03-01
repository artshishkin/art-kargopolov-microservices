keytool -genkeypair -alias apiEncryptionKey -keyalg RSA \
-dname "CN=Artem Shyshkin,OU=API Developmanet,O=shyshkin.net,L=Kyiv,S=Kyiv,C=UA" \
 -keypass 1q2w3e4r -keystore apiEncryptionKey.keystore.jks -storepass 1q2w3e4r

# CN - Common Name - any value
# OU - Organization Unit - Department name (any value)
# O - Organization
# L - Locality - can be city
# S - State name
# C - Country code