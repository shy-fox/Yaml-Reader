# The class `StringType`

## Contents:

1. [Overview](#overview)
2. [Examples](#examples)
3. [Methods](#methods)
   - All methods

---

## Overview

> Contents still are subject to changes

The class `StringType` is a child class of the generalized `AbstractType` class, but it still contains
similarities to Java's built-in `String`, having methods such as `replace`, `format` or `substring`.
One major difference from Java's built-in type is the fact that `StringType` implements the _interface_
`Iterable`, see the header of the class:

```java
public final class StringType extends AbstractType<String> implements Iterable<Character> {

}
```

It is also described by its own `JavaDoc` comment, see below:

> Instances of the class <code>StringType</code> carry a value of type <code>String</code> and behave similar to it,
> as they contain a string as value and allow for easy access via similar methods of <code>String</code>.
> <p></p>
> However, unlike the class <code>String</code>, this object it iterable and allows for other things <code>String</code>
> can't do, such as inserting <code>characters</code> or <code>strings</code> at a specific point.
> As already mentioned, the key difference to <code>String</code> is that this class implements <code>Iterable</code>, therefore
> marking it as <em>iterable</em>, whereas <code>Strings</code> can only be iterated through by using a normal or enhanced <code>for loop</code>.
> <p></p>
> Valid arguments this object accepts for constructors are:
> <ul>
>     <li><code>&lt;no parameter&gt;</code></li>
>     <li><code>char</code></li>
>     <li><code>char[]</code></li>
>     <li><code>String, boolean</code></li>
>     <li><code>String</code></li>
>     <li><code>StringBuilder</code></li>
> </ul>

## Examples
Below is an example for creating and interacting with a `StringType` object.

```java
import io.shiromi.saml.types.StringType;

public class Main {
    final static StringType s = new StringType("Content");

    public static void main(String[] args) {
        System.out.println(s);
    }
}
```
The result of this example should look like this:
<details>
<summary><code>Click to reveal result</code></summary>

```
StringType { value: "Content" }
```

</details>

---
In this example, we create a new `StringType` and from an array of values, and printing it to the console.

```java
import io.shiromi.saml.types.StringType;

public class Main {
    public static void main(String[] args) {
        String[] values = { "foo", "bar", "boo" };
        StringType s = StringType.fromArray(values, ':');
        System.out.println(s);
    }
}
```
The result of this example should look like this:
<details style="cursor: pointer" >
<summary><code>Click to reveal result</code></summary>

```
StringType { value: "foo:bar:boo" }
```

</details>

---
In the next example we will create a `StringType` containing a small text and printing each character to the console,
there are two ways to do so, both will be shown in the example

```java
import io.shiromi.saml.types.StringType;

public class Main {
    public static void main(String[] args) {
        StringType s = new StringType("Here's some text.");
        // Method 1
        for (char c : s) System.out.println(c);
        // Method 2
        s.forEachChar(System.out::println); // or s.forEachChar(c -> System.out.println(c));
    }
}
```
Either of these methods should result in this output:
<details style="cursor: pointer" >
<summary><code>Click to reveal result</code></summary>

```
H
e
r
e
'
s

s
o
m
e

t
e
x
t
.
```

</details>

---
In the following example, we use the methods `format`, `replace` and `split` which behave similar to the variants of `String`
```java
import io.shiromi.saml.types.StringType;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        StringType s = new StringType("Here's foo some foo text foo to foo modify.");
        StringType[] t = s.split("foo");
        String u = s.getValue();
        System.out.println(s);
        System.out.println(s.replaceAll("foo ", ""));
        System.out.println(StringType.format(u + " $s", "With some more text added!"));
        System.out.println(Arrays.toString(t));
    }
}
```
This should result in this output:
<details style="cursor: pointer">
<summary><code>Click to reveal result</code></summary>

```
StringType { value: "Here's foo some foo text foo to foo modify." }
StringType { value: "Here's some text to modify." }
StringType { value: "Here's foo some foo text foo to foo modify. With some more text added!" }
[StringType { value: "Here's " }, StringType { value: " some " }, StringType { value: " text " }, StringType { value: " to " }, StringType { value: " modify." }]
```

</details>

## Methods
Below is a list of all `public` and `public static` methods inside `StringType`
<ol>
<li><a href="#"><code>contains(char)</code></a></li>
<li><a href="#"><code>find(char)</code></a></li>
<li><a href="#"><code>find(String)</code></a></Li>
<li><a href="#"><code>findLast(char)</code></a></li>
<li><a href="#"><code>findAll(char)</code></a></li>
<li><a href="#"><code>findAll(String)</code></a></Li>
<li><a href="#"><code>substring(int)</code></a></li>
<li><a href="#"><code>substring(int, int)</code></a></li>
<li><a href="#"><code>splitEach(int)</code></a></li>
<li><a href="#"><code>matches(String)</code></a></Li>
<li><a href="#"><code>indexOf(char)</code></a></li>
<li><a href="#"><code>indexOf(String)</code></a></Li>
<li><a href="#"><code>indexOf(string, int)</code></a></li>
<li><a href="#"><code>lastIndexOf(String)</code></a></Li>
<li><a href="#"><code>lastIndexOf(char)</code></a></li>
<li><a href="#"><code>indexesOf(char)</code></a></li>
<li><a href="#"><code>remove(int)</code></a></li>
<li><a href="#"><code>remove(char)</code></a></li>
<li><a href="#"><code>removeChars(int, int)</code></a></li>
<li><a href="#"><code>removeChars(char[])</code></a></li>
<li><a href="#"><code>removeString(String)</code></a></Li>
<li><a href="#"><code>insert(char, int)</code></a></li>
<li><a href="#"><code>insert(String, String)</code></a></Li>
<li><a href="#"><code>insert(string, int)</code></a></li>
<li><a href="#"><code>add(char, int)</code></a></li>
<li><a href="#"><code>add(char)</code></a></li>
<li><a href="#"><code>append(char)</code></a></li>
<li><a href="#"><code>append(StringType)</code></a></Li>
<li><a href="#"><code>append(String)</code></a></Li>
<li><a href="#"><code>appendLine(String)</code></a></Li>
<li><a href="#"><code>append(char[]</code></a></li>
<li><a href="#"><code>append(Object)</code></a></Li>
<li><a href="#"><code>appendRepeat(char, int)</code></a></li>
<li><a href="#"><code>repeatAppend(string, int)</code></a></li>
<li><a href="#"><code>charAt(int)</code></a></li>
<li><a href="#"><code>startsWith(String)</code></a></Li>
<li><a href="#"><code>endsWith(String)</code></a></Li>
<li><a href="#"><code>endsWith(char)</code></a></li>
<li><a href="#"><code>length()</code></a></li>
<li><a href="#"><code>toCharArray()</code></a></li>
<li><a href="#"><code>toCharArray(int, int)</code></a></li>
<li><a href="#"><code>toArray()</code></a></li>
<li><a href="#"><code>toArray(int, int)</code></a></li>
<li><a href="#"><code>getLast()</code></a></li>
<li><a href="#"><code>forEachChar(StringIterator)</code></a></Li>
<li><a Href="#"><code>forEachChar(Int, StringIterator)</code></a></Li>
<li><a href="#"><code>isEmpty()</code></a></li>
<li><a href="#"><code>clear()</code></a></li>
<li><a href="#"><code>split(string, int)</code></a></li>
<li><a href="#"><code>split(pattern, int)</code></a></li>
<li><a href="#"><code>split(Pattern)</code></a></Li>
<li><a href="#"><code>split(String)</code></a></Li>
<li><a href="#"><code>split(char, int)</code></a></li>
<li><a href="#"><code>split(char)</code></a></li>
<li><a href="#"><code>array(String, String...)</code></a></Li>
<li><a href="#"><code>array(String[])</code></a></Li>
<li><a href="#"><code>fromArray(String[], String)</code></a></Li>
<li><a href="#"><code>fromArray(String[])</code></a></Li>
<li><a href="#"><code>fromArray(string[], delimiter)</code></a></li>
<li><a href="#"><code>fromArray(String[], StringType)</code></a></Li>
<li><a href="#"><code>oin(String[])</code></a></Li>
<li><a href="#"><code><em>format</em>(String, Object...)</code></a></li>
<li><a href="#"><code>replace(char, char)</code></a></li>
<li><a href="#"><code>replaceAll(char, char)</code></a></li>
<li><a href="#"><code>toBuffer()</code></a></li>
<li><a href="#"><code>replace(String, String)</code></a></Li>
<li><a href="#"><code>replaceAll(String, String)</code></a></Li>
<li><a href="#"><code>iterator()</code></a></li>
<li><a href="#"><code>toString()</code></a></li>
<li><a href="#"><code>hashCode()</code></a></li>
<li><a href="#"><code>clone()</code></a></li>
<li><a href="#"><code>copy()</code></a></li>
<li><a href="#"><code>equals(Object)</code></a></li>
<li><a href="#"><code>repeat(int)</code></a></li>
</ol>

---
<span id="#contains_char"></span>
```java
public boolean contains(char c)
```

<blockquote>
Checks whether this object contains the specified substring <br>

<table>
<tbody>
<tr style="border: none">
<td style="border: none; text-align: left;">Params:</td>
<td style="border: none; text-align: left;"><code>substring</code> &ndash; the <code>substring</code> to look for</td>
</tr>
<tr style="border: none">
<td style="border: none; text-align: left">Returns:</td>
<td style="border: none; text-align: left">whether this object contains the specified <code>String</code> or not</td>
</tr>
<tr style="border: none">
<td style="border: none; text-align: left">See Also:</td>
<td style="border: none; text-align: left"><a href="#"><code>contains(char)</code></a></td>
</tr>
</tbody>
</table>
</blockquote>

---

### All Methods
