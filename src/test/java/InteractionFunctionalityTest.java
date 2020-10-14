import MyMockLibrary.MyMockingLibrary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InteractionFunctionalityTest {
    @Test
    public void InteractionCounterForClassReturnsZeroWhenNoInteractionHasBeenMade() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();

        assertEquals(0,MyMockingLibrary.classInteractionCount(mockedObject));
    }

    @Test
    public void InteractionCounterForClassReturnsCorrectCountWhenInteractionHasBeenMadeOnServeralClasses() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        MyMockingLibrary<Example> mock2 = new MyMockingLibrary(IExample.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();
        IExample mockedObject2 = mock2.getMockObject();

        // interacting just with Example class
        mockedObject.ExampleMethod();

        assertEquals(1,MyMockingLibrary.classInteractionCount(mockedObject));
        assertEquals(0,MyMockingLibrary.classInteractionCount(mockedObject2));
    }

    @Test
    public void InteractionCounterForClassReturnsCorrectCountWhenNumberOfInteractionHasBeenMade() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();

        int max = 20; int min = 10;
        int random = (int)(Math.random() * (max - min + 1) + min);

        for (int i = 0; i < random; i++){
            mockedObject.ExampleMethod();
        }

        assertEquals(random,MyMockingLibrary.classInteractionCount(mockedObject));
    }

    @Test
    public void InteractionCounterForMethodReturnsZeroWhenNoInteractionHasBeenMade() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();

        assertEquals(0,MyMockingLibrary.methodInteractionCount(mockedObject, "ExampleMethod", int.class));
    }

    @Test
    public void InteractionCounterForMethodReturnsCorrectCountWhenInteractionHasBeenMadeWithServeralMethods() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();

        mockedObject.ExampleMethod();
        mockedObject.ExampleMethod(1);

        assertEquals(1,MyMockingLibrary.methodInteractionCount(mockedObject, "ExampleMethod", int.class));
    }

    @Test
    public void InteractionCounterForMethodReturnsCorrectCountWhenNumberOfInteractionHasBeenMade() {
        MyMockingLibrary<Example> mock = new MyMockingLibrary(Example.class);
        mock.mock("ExampleMethod", "testing");

        Example mockedObject = mock.getMockObject();

        int max = 20; int min = 10;
        int random = (int)(Math.random() * (max - min + 1) + min);

        for (int i = 0; i < random; i++){
            mockedObject.ExampleMethod(1);
        }

        assertEquals(random,MyMockingLibrary.methodInteractionCount(mockedObject, "ExampleMethod", int.class));
    }

}
