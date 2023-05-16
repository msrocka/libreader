# libreader

This is an experimental implementation of a protocol for reading data from an openLCA library. It abstracts away how data are retrieved from a library to support the following use cases:

* [x] reading matrix and index data directly from disk (`DirectLibReader`)
* [x] caching data (`CachingLibReader`)
* [x] calculating columns of the inverse or intensities on demand without the need for pre-calculation (`SolvingLibReader`)
* [ ] support for running Monte Carlo simulations with library data (`SimulatingLibReader`)
* [ ] support for libraries with encrypted data (`DecryptingLibReader`)
* [ ] support for libraries with data quality information attached

Other implementations are possible by implementing the `LibReader` interface. The readers can be chained together to create readers with combined functionalities which are then added to a registry, e.g.:

```java
var reader =
  SolvingLibReader.of(
    CachingLibReader.of(
      DecryptingLibReader.of(
        DirectLibReader.of(database, library), secret)));
libRegistry.add("library xy", reader);
```

The plan is to pass such a registry then to the calculation back-end of openLCA which then retrieves the respective `LibReader` implementation for a library from that registry:

```java
var setup = CalculationSetup.of(system)
  // ...
  .withLibraries(libRegistry);
var result = SystemCalculator.of(db, setup)
  .calculate();
// use result ...
result.dispose(); // will also dispose library resources
```
