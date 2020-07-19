# autoswitch-api

API for the AutoSwitch mod.

To add this API to your dev enviroment:

In build.gradle add ```repositories {
	maven { url 'https://jitpack.io' }
}```
 
 And in ```dependencies {...}``` add ```modImplementation 'com.github.dexman545:autoswitch-api:-SNAPSHOT'```
 
 In your fabric.mod.json, add the following:
 ```json
 "entrypoints": {
  "autoswitch": [ "path-to-class-impl-the-API-interface" ]
}
 ```
