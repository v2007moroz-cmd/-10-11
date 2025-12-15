import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {


    static class Person {
        final int id;
        final String name;
        final String city;
        final int age;

        Person(int id, String name, String city, int age) {
            this.id = id;
            this.name = name;
            this.city = city;
            this.age = age;
        }

        int age() { return age; }
        String city() { return city; }

        @Override
        public String toString() {
            return id + " " + name + " " + city + " " + age;
        }
    }

    public static void main(String[] args) {



        int N = 10_000;

        List<Person> list = new ArrayList<>();
        Set<Person> set = new HashSet<>();
        Map<Integer, Person> map = new HashMap<>();

        String[] cities = {"Kyiv", "Lviv", "Odesa", "Dnipro"};

        for (int i = 0; i < N; i++) {
            Person p = new Person(
                    i,
                    "Name" + i,
                    cities[i % cities.length],
                    18 + (i % 50)
            );
            list.add(p);
            set.add(p);
            map.put(p.id, p);
        }

        System.out.println("Loaded records: " + list.size());

 
        List<String> namesFromKyiv =
                list.stream()
                    .filter(p -> p.city().equals("Kyiv"))
                    .map(p -> p.name)
                    .limit(5)
                    .toList();

        Map<String, List<Person>> byCity =
                list.stream()
                    .collect(Collectors.groupingBy(Person::city));

        double avgAge =
                list.stream()
                    .collect(Collectors.averagingInt(Person::age));

        Map<String, Long> countByCity =
                list.stream()
                    .collect(Collectors.groupingBy(
                            Person::city,
                            Collectors.counting()
                    ));



        Optional<Person> maybePerson =
                Optional.ofNullable(map.get(500));

        int ageSafe =
                maybePerson
                        .map(Person::age)
                        .orElse(-1);

        Optional<Person> missing =
                Optional.ofNullable(map.get(50_000));

        String result =
                missing
                        .map(p -> p.name)
                        .orElse("NOT FOUND");



        benchmarkLists();
        benchmarkMaps();



        System.out.println("\n=== STREAM RESULTS ===");
        System.out.println("Names from Kyiv (sample): " + namesFromKyiv);
        System.out.println("Average age: " + avgAge);
        System.out.println("Count by city: " + countByCity);

        System.out.println("\n=== OPTIONAL RESULTS ===");
        System.out.println("Existing person age: " + ageSafe);
        System.out.println("Missing person result: " + result);

        conclusions();
    }

    static void benchmarkLists() {
        int N = 100_000;

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long t1 = System.nanoTime();
        for (int i = 0; i < N; i++) arrayList.get(i);
        long t2 = System.nanoTime();

        for (int i = 0; i < N; i++) linkedList.get(i);
        long t3 = System.nanoTime();

        System.out.println("\n=== LIST BENCHMARK ===");
        System.out.println("ArrayList get():  " + (t2 - t1) + " ns");
        System.out.println("LinkedList get(): " + (t3 - t2) + " ns");
    }


    static void benchmarkMaps() {
        int N = 100_000;

        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();

        for (int i = 0; i < N; i++) {
            hashMap.put(i, i);
            treeMap.put(i, i);
        }

        long t1 = System.nanoTime();
        for (int i = 0; i < N; i++) hashMap.get(i);
        long t2 = System.nanoTime();

        for (int i = 0; i < N; i++) treeMap.get(i);
        long t3 = System.nanoTime();

        System.out.println("\n=== MAP BENCHMARK ===");
        System.out.println("HashMap get(): " + (t2 - t1) + " ns");
        System.out.println("TreeMap get(): " + (t3 - t2) + " ns");
    }


    static void conclusions() {
        System.out.println("""
                
=== ВИСНОВКИ ===
• ArrayList значно швидший за LinkedList при доступі за індексом.
• LinkedList доцільний лише для частих вставок/видалень у середині списку.
• HashMap забезпечує O(1) доступ і є оптимальним для ключових операцій.
• TreeMap повільніший, але зберігає впорядкованість ключів.
• Stream API дозволяє компактно описувати фільтрацію, агрегацію та групування.
• Optional зменшує ризик NullPointerException і покращує читабельність коду.
• Для більшості бізнес-сценаріїв оптимальна комбінація: ArrayList + HashMap.
""");
    }
}
