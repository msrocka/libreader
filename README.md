# libreader

An experimental implementation of a `LibReader` protocol for openLCA.
It abstracts away how library matrix data are retrieved from an
openLCA data library. The idea is that this can be then used to
support Monte Carlo Simulations with data libraries, e.g. by
providing something like a `SimulatingLibReader`. Also, we could
just mount things like in-memory libraries, which could be useful
in some cases.

Also, encrypted libraries could be handled with this?

```java
var reader = EncryptedLibReader.of(secret, wrappedReader);
```
