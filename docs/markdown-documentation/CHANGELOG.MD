# Changelogs of `SAML (Yaml-Reader)`

## Overview
- [Version 1.0*](#version-10)
- [Version 1.1*](#version-11)
- [Version 1.2*](#version-12)
- [Version 1.3*](#version-13)
- [Version 1.4*](#version-14)
- [Version 1.5](#version-15-public--release-)
- [Version 1.6.1](#version-161--release-)
- [Version 1.6.2](#version-162--release-)
- [Version 1.7](#version-17--release-)
- [Version 1.7.1*](#version-171)

> NOTE: Non-public versions are marked as `Version X.X*`

## Documentation

### Version 1.0
> First version of `Yaml-Reader`, kept private

Wrote the entire reader, but put it in a single file, `Yaml.java`, the structure was somewhat like this:
<pre><code>&boxur;&boxh; abstract class Yaml
   &boxvr;&boxh; final class YamlString
   &boxvr;&boxh; final class YamlNumber
   &boxvr;&boxh; final class YamlObject
   &boxv;   &boxur;&boxh; interface YamlObjectIterator
   &boxur;&boxh; final class YamlArray
       &boxur;&boxh; interface YamlArrayIterator
</code></pre>
Everything was an inner class of `Yaml` and I didn't think I would publish it, the entire reason
I wrote this project was for an RPG library I'm also currently working on, but it's not done yet

[< Back to top](#overview)

---
### Version 1.1
> The first version actually being able to write to a file.
> <p></p>
> 
> Changed some stuff around and added the very important `toString()` function to each type,
> in addition to adding a second file called `YamlFile.java`, so the structure changed to:
> <pre><code>&boxvr;&boxh; abstract class Yaml
> &boxv;   &boxvr;&boxh; final class YamlString
> &boxv;   &boxvr;&boxh; final class YamlNumber
> &boxv;   &boxvr;&boxh; final class YamlObject
> &boxv;   &boxv;   &boxur;&boxh; interface YamlObjectIterator
> &boxv;   &boxur;&boxh; final class YamlArray
> &boxv;       &boxur;&boxh; interface YamlArrayIterator
> &boxur;&boxh; final class YamlFile</code></pre>
> The `toString()` function returned a valid `Yaml` representation of each object, e.g. `YamlString`
> would return `name: "value"`

[< Back to top](#overview)

---
### Version 1.2
> Changed the function `toString()` to return debug info of the object, and moved the
> code which turns the object into its `Yaml` representation to a new function called `stringify()`, e.g.
> ```java
> // ... snipped from YamlString
> @Override
> public final String toString() {
>   return String.format("%s: \"%s\"", this.name, this.value);
> }
>```
> turned into:
> ```java
> // ... snipped from YamlString
> public final String stringify() {
>   return String.format("%s: \"%s\"", this.name, this.value);
> }
> @Override
> public final String toString() {
>   return String.format("YamlString{name: %s, value: %s}", this.name, this.value);
> }
> ```

[< Back to top](#overview)

---
### Version 1.3
> Started splitting each inner class from `Yaml` into separate files, once again the structure changed:
> <pre><code>&boxvr;&boxh; abstract class Yaml
> &boxvr;&boxh; final class YamlString
> &boxvr;&boxh; final class YamlNumber
> &boxvr;&boxh; final class YamlObject
> &boxv;   &boxur;&boxh; interface YamlObjectIterator
> &boxvr;&boxh; final class YamlArray
> &boxv;   &boxur;&boxh; interface YamlArrayIterator
> &boxur;&boxh; final class YamlFile</code></pre>
> and did some small bug fixes here and there

[< Back to top](#overview)

---
### Version 1.4
> The last private version of `Yaml-Reader` <br />
> moved the different _elements_ to a new package, called `util`:
> <pre><code>&boxvr;&boxh; abstract class Yaml
> &boxvr;&boxh; final class YamlFile
> &boxur;&boxh; util
>     &boxvr;&boxh; final class YamlString
>     &boxvr;&boxh; final class YamlNumber
>     &boxvr;&boxh; final class YamlObject
>     &boxv;   &boxur;&boxh; interface YamlObjectIterator
>     &boxur;&boxh; final class YamlArray
>         &boxur;&boxh; interface YamlArrayIterator</code></pre>
> and implemented the first version of `fromString(String)` and `read(File f)`

[< Back to top](#overview)

---
### Version 1.5-public [(Release)](https://github.com/shy-fox/Yaml-Reader/releases/tag/v1.5-public)
> First public release of `Yaml-Reader`
> <p></p>
> Published project and files to a <a href="https://github.com/shy-fox/yaml-reader">GitHub Repo</a> and 
> did some minor changes, mainly adding <code>YamlNull</code>

[< Back to top](#overview)

---
### [Version 1.6.1](https://github.com/shy-fox/Yaml-Reader/commit/dc44e7dcb0e92c3ecad435336f90ddb42a7477fe) [(Release)](https://github.com/shy-fox/Yaml-Reader/releases/tag/v1.6-a.pre)
> Changed some stuff inside `fromString(String)` inside `Yaml` and updated `absoluteSize()` inside `YamlObject`

[< Back to top](#overview)

---
### [Version 1.6.2](https://github.com/shy-fox/Yaml-Reader/commit/7214d07b3d8901a60c77f2dbf3a11792aadd7a61) [(Release)](https://github.com/shy-fox/Yaml-Reader/releases/tag/v-1.6-b)
> Added two `Exception` classes, added `count` functions to `YamlObject`, added
> a `toNull([Yaml)` function to `Yaml`

[< Back to top](#overview)

---
### [Version 1.7](https://github.com/shy-fox/Yaml-Reader/commit/f3d81ba10ef1217c93e5f2bf52c27bdd150e1d2b) [(Release)](https://github.com/shy-fox/Yaml-Reader/releases/tag/V1.7)
> Changed allowed naming schemes for objects that were being parsed, added functions to get certain types of objects via names
> to `YamlObject` and added `isEmpty([YamlString)` and `equals(String[, YamlString)` to `YamlString`

[< Back to top](#overview)

---
### Version 1.7.1
> Did some major changes to `YamlNumber` as it now was split into `YamlFloat` and `YamlInt` as well
> as adding a `Utils` class for `Math` and `Array` utilities, like filling and arithmetic operations

[< Back to top](#overview)
