package tests.facade_pattern;

public class FacadePatternTest {
    private static final String FILE_NAME = "test.txt";
    private static final String FILE_CONTENT = "Hello, World!";

    public static void treatFile() {
        FileFacade fileFacade = new FileFacade();
        fileFacade.writeFile(FILE_NAME, FILE_CONTENT);
        String readContent = fileFacade.readFile(FILE_NAME);
        System.out.println(readContent);
    }
}
