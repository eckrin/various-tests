package tests.functional_interface;

public class FunctionalInterfaceTest {

    public static void functionalInterfaceTest() {
        System.out.println(getFunctionalInterface((i1, i2) -> i1*i2));
    }

    static int getFunctionalInterface(FunctionalInterfaceEx ex) {
        return ex.defaultFunctionInInterface(1, 2);
    }
}
