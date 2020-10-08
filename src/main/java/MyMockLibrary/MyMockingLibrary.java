package MyMockLibrary;

import net.openhft.compiler.CompilerUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class MyMockingLibrary<TMockable> {
    private final Class<TMockable> tMockableClass;

    public static Map<String, Integer> _mockedClassInteractionCounter = new HashMap<>();

    private Map<String, Object> _mockedMethodInterceptors = new HashMap<>();

    public MyMockingLibrary(Class<TMockable> tMockableClass) {
        this.tMockableClass = tMockableClass;
    }

    public static <TMockable> int classInterActionCount(TMockable mockedObject) {
        if(_mockedClassInteractionCounter.containsKey(mockedObject.getClass().getName())){
            return _mockedClassInteractionCounter.get(mockedObject.getClass().getName());
        }
        return 0;
    }

    public static <TMockable> int classInterActionCount(TMockable mockedObject, String exampleMethod, Class... parameters) {
        if(_mockedClassInteractionCounter.containsKey(mockedObject.getClass().getName())){
            StringBuilder parameterNames = new StringBuilder();
            for (Class parameter : parameters) {
                parameterNames.append(parameter.getName().replace(".", ""));
            }
            return _mockedClassInteractionCounter.get(mockedObject.getClass().getName() + exampleMethod + parameterNames);
        }
        return 0;
    }

    public static void increaseInteractionCount(String name) {
        if(_mockedClassInteractionCounter.containsKey(name)){
            _mockedClassInteractionCounter.put(name, _mockedClassInteractionCounter.get(name) + 1);
        }else{
            _mockedClassInteractionCounter.put(name, 1);
        }
    }

    public void mock(String methodName, Object result){
        try{
            Method methodToMock = tMockableClass.getMethod(methodName);
            _mockedMethodInterceptors.put(methodName, result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void mock(String methodName, Object result, Class... parameters){
        try{
            // if case there is no such method
            Method methodToMock = tMockableClass.getMethod(methodName, parameters);
            StringBuilder parameterNames = getParameterNames(parameters);
            _mockedMethodInterceptors.put(methodName + parameterNames, result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public TMockable getMockObject(){
        _mockedClassInteractionCounter = new HashMap<>();
        String className = tMockableClass.getName();
        String newTypeName = String.format("%sProxy",className);
        String javaCode = CreateSourceCode(className, newTypeName);
        Class mockedClass = null;
        try {
            mockedClass = CompilerUtils.CACHED_COMPILER.loadFromJava(newTypeName, javaCode);
            return (TMockable) mockedClass.getDeclaredConstructor(Map.class).newInstance(_mockedMethodInterceptors);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String CreateSourceCode(String className, String newTypeName){

        StringBuilder sourceCode = new StringBuilder();
        sourceCode.append("import java.util.Map;\n");
        sourceCode.append("import MyMockLibrary.MyMockingLibrary;\n");
        sourceCode.append("import java.util.function.Function;\n");

        String implementation = "extends";
        if(tMockableClass.isInterface()){
            implementation = "implements";
        }

        sourceCode.append(String.format("public class %s %s %s {\n", newTypeName,implementation,className));
        sourceCode.append("private Map<String, Object> _methodIntercetors;\n");
        sourceCode.append(String.format("public %s(Map<String, Object> methodIntercetors) {\n_methodIntercetors = methodIntercetors;\n}", newTypeName));
        sourceCode.append("public Object InterceptMethod(String methodName) throws NoSuchMethodException {\nif(!_methodIntercetors.containsKey(methodName)){\nthrow new NoSuchMethodException();\n}\nreturn _methodIntercetors.get(methodName);\n}");

        for (Method method: tMockableClass.getDeclaredMethods()) {
            writeMethod(method, sourceCode);
        }

        sourceCode.append("}\n");
        return sourceCode.toString();
    }

    private void writeMethod(Method mockableMethod, StringBuilder sourceCode){
        String returnType = mockableMethod.getReturnType().getTypeName();
        String methodName = mockableMethod.getName();
        StringBuilder methodNameWithParameters = new StringBuilder(methodName);

        sourceCode.append(String.format("public %s %s(", returnType, methodName));

        // append parameters
        int i = 0;
        for (Parameter parameter: mockableMethod.getParameters()) {
            if(i++ > 0){
                sourceCode.append(",");
            }

            // append parameter type names to the end of the name
            // this will solve the problem of mocking method with same name but different parameters
            methodNameWithParameters.append(parameter.getType().getName().replace(".", ""));
            sourceCode.append(String.format("%s %s", parameter.getType().getName(), parameter.getName()));
        }
        sourceCode.append("){\n");

        sourceCode.append("MyMockingLibrary.increaseInteractionCount(this.getClass().getName());");
        sourceCode.append(String.format("MyMockingLibrary.increaseInteractionCount(this.getClass().getName() + \"%s\");", methodNameWithParameters));

        if(!returnType.equals("void")){
            sourceCode.append(String.format("try{\n" +
                    "            Object result = InterceptMethod(\"%s\");\n" +
                    "            return (%s) result;\n" +
                    "        }catch (NoSuchMethodException e){\n" +
                    "            return null;\n" +
                    "        }", methodNameWithParameters.toString(),returnType));
        }

        sourceCode.append("}\n");
    }

    @NotNull
    private StringBuilder getParameterNames(Class[] parameters) {
        StringBuilder parameterNames = new StringBuilder();
        for (Class parameter : parameters) {
            parameterNames.append(parameter.getName().replace(".", ""));
        }
        return parameterNames;
    }

}
