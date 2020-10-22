# MockingLibrary

### Overview

This mocking library is personal project with my take on how mocking library could work. 
It creates a mocked object (during the runetime) that extends or implements given object with 
all its methods. Library also can provide useful insights on how many times mocked class or method was used. 

### Usage

Firstly you need to create MyMockingLibrary object with a class that you want to be mocked

```
MyMockingLibrary<IExample> mock = new MyMockingLibrary(IExample.class);
```

To mock a method you can call 'mock' method with the name and return value as an arguments
```
mock.mock("ExampleMethodName", "valueToReturn");

// If method takes parameters you should include them at the end 
mock.mock("ExampleMethodName", "valueToReturn", int.class, String.class);

```

To retrieve mocked object you should call getMockObject method

```
IExample mockedObject = mock.getMockObject();
```

### Additional functionality

#### Retrieving the count of interactions for mocked classes

To retrieve the count of interactions you should call static method classInterActionCount with
parameters:

```
MyMockingLibrary.classInterActionCount(mockedObject)'
```

#### Retrieving the count of interactions for mocked methods

To retrieve the count of interactions you should call static method methodInterActionCount with 
parameters:

```
MyMockingLibrary.methodInterActionCount(mockedObject, "ExampleMethodName", ExampleMethodParameters...)'
```

Example
```
MyMockingLibrary.methodInterActionCount(mockedObject, "ExampleMethodName", int.class, String.class)'
```


