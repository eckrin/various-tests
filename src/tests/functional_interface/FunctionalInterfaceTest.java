package tests.functional_interface;

@FunctionalInterface
public interface FunctionalInterfaceTest {
    void onlyOneAbstractFunction(int i1, int i2);
    default int defaultFunctionInInterface(int i1, int i2) {
        return 20231018;
    }
}
