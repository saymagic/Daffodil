Daffodil
===

Daffodil is an Annotation-triggered method call logging library.

Usage
---------


1. Add daffodil closure in `build.gradle`

```

daffodil {
    enabled true
}

```

2. Add `@ Daffodil` Annotation on methods, method call's detail info will automatically be recorded.

```
@Daffodil
public int max(int a, int b) {
    return Math.max(a, b);
}
```

As the method max invoked, log is printed like following:

```
I/MainActivity: max(1607348716,366634143) = 1607348716 {1ms, main}
```

3. Enable/Disable daffodil in runtime

```
DaffodilPrinter.setEnabled(true/false);
```

4. Customized printer

```
DaffodilPrinter.setPrintDelegate(new DaffodilPrinter.DaffodilPrinterDelegate() {
      @Override
      public void printMethod(MethodInfo info) {

      }

      @Override
      public void log(String msg) {

      }
});
```

Download
----

* add the plugin to your top build script:

```
buildscript {
  repositories {
    jcenter()
   }
  dependencies {
    classpath 'tech.saymagic:daffodil:1.0.0'
  }
}
```
* apply plugin in your project:

```
apply plugin: 'tech.saymagic.daffodil'
```

* add runtime library in your project's dependencies:

```
dependencies {
  compile 'tech.saymagic: daffodil-lib:1.0.0'
}
```

License
--------

    Copyright 2017 Saymagic

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
