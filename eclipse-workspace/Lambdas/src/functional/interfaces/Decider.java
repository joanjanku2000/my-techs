package functional.interfaces;
		
@FunctionalInterface
public interface Decider {
	boolean decide(String str);
	default void someOther() {
		System.out.println("Default method");
	}
}
