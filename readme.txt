Folder Contains:
----------------
* readme.txt          - this readme file

* akeystore.jks       - keystore player A - sender
* bkeystore.jks       - keystore player B - receiver
* generate_store.sh   - script to run the keystore if needed, contains the commands used to create the stores

* plaintext.txt       - input file for the java program
* run.txt             - commands on how to run the program
* secureApp.jar       - the java program

* keystore_run.png    - image of the run of the script generate_store.sh, used to create the keystore
* successfull_run.png - image of a successful run 

* secureApp           - folder containing the project with all the folder used to create the java project.

-------------------------------------------------------------------------------------------------------------------------

How To Run?
-----------
1. First make sure the folder contains this next files:
   * plaintext.txt
   * akeystore.jks
   * bkeystore.jks

   If this file are not present for any reason, you can generate the keystore files using the script generate_store.sh, simply run ./generate_store.sh and type yes when needed.

2. Go to run.txt, you can run the commands by coping the commands and running them manually or by running the file simply by typing: ./run.txt

3. After the run you will see that the folder contains this additional files:
   * encrypted.txt - the encrypted file, that is the output of the encryption. will be used as the input of the decryption process.
   * decrypted.txt - the decrypted file, content is identical to the plaintext.txt
   * info.bin - information sent from the "sender" to the "receiver" containing the needed information to decipher the file.

-------------------------------------------------------------------------------------------------------------------------

What was done in the keystore creation?
---------------------------------------
1. created akey for side A and bkey for side B.
2. created two keystores, one for the sender A named: akeystore.jks, and for the receiver B named: bkeystore.jks.
3. each side sends the other one his certificate, generated from their private key.
4. used RSA encryption key with size of 2048 bits.
5. Tried to really choose real passwords based on best practice:
	* easily remembered
	* at least 8 characters long
	* a mixture of at least 3 of the following: upper case letters, lower case letters, digits and symbols
	* not listed in a dictionary of any major language

-------------------------------------------------------------------------------------------------------------------------

What are the params of the java program?
----------------------------------------
For encrypt:
	java -jar secureApp.jar encrypt "AStPass()" akey "ASecPass@#" bkeyremote

	1. encrypt - word used to identify we want to run the encrypt process.
	2. "AStPass()" - password for player A, the sender keystore
	3. akey - alias name for sender private key
	4. "ASecPass@#" - password for the sender private key
	5. bkeyremote - certificate alias of player B, the receiver.


For decrypt:
	java -jar secureApp.jar decrypt "BSt[]Pass" bkey "BSecPass$%" akeyremote

	1. decrypt - word used to identify we want to run the decrypt process.
	2. "BSt[]Pass" - password for player B, the receiver keystore
	3. bkey - alias name for receiver private key
	4. "BSecPass$%" - password for the receiver private key
	5. bkeyremote - certificate alias of player A, the receiver.	

	
