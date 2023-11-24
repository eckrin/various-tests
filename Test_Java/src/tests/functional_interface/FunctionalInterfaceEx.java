package tests.functional_interface;

@FunctionalInterface
public interface FunctionalInterfaceEx {
    int onlyOneAbstractFunction(int i1, int i2);
    default int defaultFunctionInInterface(int i1, int i2) {
        return onlyOneAbstractFunction(i1, i2)*20231018;
    }
}
