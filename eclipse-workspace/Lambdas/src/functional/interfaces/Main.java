package functional.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	@FunctionalInterface
	interface Converter<T,R>{
		R apply(T source);
	}

	public static void main(String[] args) {
/**		Converter<String,Boolean> str2Bool 
		= (s) -> {return Boolean.parseBoolean(s);};
		
		Converter<Boolean,Integer> bool2Int = b -> b ? 1 : 0;
		
		System.out.println(bool2Int.apply(true));
		System.out.println(str2Bool.apply("true"));
*/	
		List<String> dataSource = new ArrayList();
		dataSource.add("Hello");
		dataSource.add("hi");
		dataSource.add("t");
		dataSource.add("jim");
		/**
		 * Method 1 : For Loops
		 */
		int c = 0;
		for (String s : dataSource) {
			if (s.length()==1) c++;
		}
		System.out.println("Count is (m1) : "+c);
		/**
		 * Method 2 : Stream
		 */
		long count = dataSource.stream()
				.distinct()
				.filter(str->str.length()==1)
				.count();
		System.out.println("Count = "+count);
		/** 
		 * Sysout elements on condition 
		 */
		
		dataSource
			.stream()
			.filter(str -> str.length()>1)
			.forEach(s -> System.out.println(" "+s));
		
		/**
		 * Maping on a condition
		 */
		String[] words = {"the","quick","brown","fox"};
		List<String> upper = Arrays.stream(words)
					.map(w->w.toUpperCase())
					.collect(Collectors.toList());
			
		System.out.println("Upper = "+upper);
		
		/**
		 * Stream creations
		 */
		
		Stream<Integer> intStream = Stream.of(0,1,2,3,4,5,2,3,4,5,5,6,6);
		intStream.distinct().forEach(System.out::println);
		
		Stream.Builder<Double> theBuilder = Stream.<Double> builder();
		Stream<Double> decimals = theBuilder.add(2.1).add(2.3).build();
		
		int sum = decimals
				.mapToInt(d -> (int) Math.round(d))
				.sum();
		
		System.out.println("Sum is "+sum);
		/**
		 * Stream iterate -> infinte stream
		 * but limit later
		 */
		Stream<Integer> infinteStream = Stream.iterate(10, n -> n *10);
		int sum2 = infinteStream
				.limit(10) //intermediate operation
				.mapToInt(i -> i)
				.sum(); //final operation
		
		//infinite stream 2
		Stream<Random> infiniteStream = Stream.generate(() -> new Random());
		
		/**
		 * Collection converting
		 */
		
		Stream<String> stream = Stream.of("the","chicken","nugget","day");
		
		List<String> asList = stream
				.peek(s -> System.out.println(" "+s))
				.collect(Collectors.toList());
		System.out.println(asList);
		
		// the original stream cannot be used because 
		// it was closed with the collect operation
		Set<String> asSet = asList.stream().collect(Collectors.toSet());
		System.out.println(asSet);
		
		Stream<String[]> stream2 = Stream.of(
				new String[][] {
						{"Hamlet Act 3 Scene 1","To be or not to be"},
						{"Romeo & Juliet Act 2 Scene 2","Romeo! Romeo! Whereore art thou, Romeo?"},
						{"Richard III Act 1 Scene 1","Now is the winter of our discontent"},
						{"Henry IV Pt 2 Act 3 Scene 2","A man can die but once"},
						{"The merchant of Venice Act 2 Scene 7","All the glisters is not gold"}
				});
		
		Map<String,String> asMap = stream2
				.parallel() // converting to parallel stream which makes use of multithreading technology
				.peek(q->System.out.println(String.format("%s %s\n", q[0],q[1])))
				.collect(Collectors.toMap(q->q[0],q->q[1]));
		
		System.out.println(asMap.get("Hamlet Act 3 Scene 1"));
		
	}
	
	

}
