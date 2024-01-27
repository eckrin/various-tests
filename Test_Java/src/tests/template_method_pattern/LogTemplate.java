package tests.template_method_pattern;

public abstract class LogTemplate<T> {

    public T log() {

        // 로깅 시작 프로세스
        System.out.println("enter class: "+this.getClass());

        // 핵심 로직 호출
        T result = call();

        // 로깅 종료 프로세스
        System.out.println("escape class: "+this.getClass());

        return result;
    }

    protected abstract T call();
}
