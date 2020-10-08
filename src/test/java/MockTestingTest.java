import MyMockLibrary.MyMockingLibrary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockTestingTest {
    @Test
    public void MockMocksMethodWithNoArguments() {
        MyMockingLibrary<IExample> mock = new MyMockingLibrary(IExample.class);
        mock.mock("ExampleMethod", "testing");

        IExample mockedObject = mock.getMockObject();

        assertEquals("testing",mockedObject.ExampleMethod());
    }

    @Test
    public void MockMocksMethodWithOneArgument() {
        MyMockingLibrary<IExample> mock = new MyMockingLibrary(IExample.class);
        mock.mock("ExampleMethod", "testing", int.class);

        IExample mockedObject = mock.getMockObject();

        assertEquals("testing",mockedObject.ExampleMethod(4));
    }

    @Test
    public void MockMocksMethodWithMultipleArguments() {
        MyMockingLibrary<IExample> mock = new MyMockingLibrary(IExample.class);
        mock.mock("ExampleMethod", "testing", int.class, String.class);

        IExample mockedObject = mock.getMockObject();

        assertEquals("testing",mockedObject.ExampleMethod(4, "testing"));
    }

    @Test
    public void MockCorrectlyMocksMethodWithSameNameButWithDiffrentResults() {
        MyMockingLibrary<IExample> mock = new MyMockingLibrary(IExample.class);
        mock.mock("ExampleMethod", "withoutParameters");
        mock.mock("ExampleMethod", "withParameters", int.class);

        IExample mockedObject = mock.getMockObject();

        assertEquals("withoutParameters",mockedObject.ExampleMethod());
        assertEquals("withParameters",mockedObject.ExampleMethod(4));
    }

    @Test
    public void MockMocksClasses() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "withoutParameters");

        Example mockedObject = mock.getMockObject();

        assertEquals("withoutParameters",mockedObject.ExampleMethod());
    }

}
