[![API](https://img.shields.io/badge/API-16%2B-red.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://travis-ci.org/wupdigital/android-maven-publish.svg?branch=master)](https://github.com/Krunal-Kevadiya/RxPermissions)
[ ![Download](https://api.bintray.com/packages/kevadiyakrunalk/MyFramework/rxpermission/images/download.svg) ](https://bintray.com/kevadiyakrunalk/MyFramework/rxpermission/_latestVersion) 
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](https://opensource.org/licenses/Apache-2.0)

# RxPermissions

* you can directly use with two line of code, no additional code required like onRequestPermissionsResult or check the granted or not.
* Code :-
```java
// - Single
   RxPermissions
    .getInstance(this)
    .checkMPermission(new PermissionResult() {
       @Override
       public void onPermissionResult(String permission, boolean granted) {
         //here is your permission list by comma separate and result of granted or not.
       }
    }, Manifest.permission.CAMERA);
// - Multiple
   RxPermissions
    .getInstance(this)
    .checkMPermission(new PermissionResult() {
        @Override
        public void onPermissionResult(String permission, boolean granted) {
          //here is your permission list by comma separate and result of granted or not.
        }
     }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
```
-> Gradle
```groovy
//add dependencies for app level build.gradle
repositories {
    jcenter()
}
dependencies {
  compile 'com.kevadiyakrunalk:rxpermissions:1.6@aar'
}
```
-> Maven
```xml
<dependency>
  <groupId>com.kevadiyakrunalk</groupId>
  <artifactId>rxpermissions</artifactId>
  <version>1.6</version>
  <type>pom</type>
</dependency>
```
