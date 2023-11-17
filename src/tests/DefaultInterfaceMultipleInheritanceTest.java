package tests;

/**
 * 1. 명시적으로 직접 다중상속할 경우 컴파일 에러가 발생한다.
 * 2. 인터페이스를 거쳐 상속받을 경우 sub interface의 default 메소드가 실행된다.
 * 3.
 */
public class DefaultInterfaceMultipleInheritanceTest implements interfaceA, interfaceB{
    public void doMultipleInheritanceTest() {
        printCommon();
    }
}

interface interfaceA {

    default void printCommon() {
        System.out.println("CommonA");
    }

}

interface interfaceB extends interfaceA {

    default void printCommon() {
        System.out.println("CommonB");
    }
}