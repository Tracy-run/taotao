## java 8 to 7

###

1.接口可添加一个默认非抽象的方法
    interface Formula {
        double calculate(int a);
        default double sqrt(int a) {
            return Math.sqrt(a);
        }
    }
    Formula接口在拥有calculate方法之外同时还定义了sqrt方法，实现了Formula接口的子类只需要实现一个calculate方法，默认方法sqrt将在子类上可以直接使用
    Formula formula = new Formula() {
        @Override
        public double calculate(int a) {
            return sqrt(a * 100);
        }
    };
    formula.calculate(100);     // 100.0
    formula.sqrt(16);           // 4.0
    
    
1.1 默认的接口

    1.1.1 Predicate接口 
    Predicate<String> predicate = (s) -> s.length() > 0;
    predicate.test("foo");              // true
    predicate.negate().test("foo");     // false
    
    Predicate<Boolean> nonNull = Objects::nonNull;
    Predicate<Boolean> isNull = Objects::isNull;
    
    Predicate<String> isEmpty = String::isEmpty;
    Predicate<String> isNotEmpty = isEmpty.negate();
     
    1.1.2 Function 接口
    Function<String, Integer> toInteger = Integer::valueOf;
    Function<String, String> backToString = toInteger.andThen(String::valueOf);
    backToString.apply("123");     // "123"
    
    1.1.3 Supplier 接口
     Supplier<Person> personSupplier = Person::new;
     personSupplier.get();   // new Person   
    
    1.1.4 Consumer 接口
    Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
    greeter.accept(new Person("Luke", "Skywalker"));
    
    1.1.5 Comparator 接口
    Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
    Person p1 = new Person("John", "Doe");
    Person p2 = new Person("Alice", "Wonderland");
    
    comparator.compare(p1, p2);             // > 0
    comparator.reversed().compare(p1, p2);  // < 0
    
    1.1.6 Optional 接口
    Optional<String> optional = Optional.of("bam");
    optional.isPresent();           // true
    optional.get();                 // "bam"
    optional.orElse("fallback");    // "bam"
    
    optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
    
    1.1.7 Stream 接口
    List<String> stringCollection = new ArrayList<>();
    stringCollection.add("ddd2");
    stringCollection.add("aaa2");
    stringCollection.add("bbb1");
    stringCollection.add("aaa1");
    stringCollection.add("bbb3");
    stringCollection.add("ccc");
    stringCollection.add("bbb2");
    stringCollection.add("ddd1");
    
    1.1.7.1  Filter 过滤
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("a"))
        .forEach(System.out::println);
    // "aaa2", "aaa1"
    
    1.1.7.2 Sort 排序
    stringCollection
        .stream()
        .sorted()
        .filter((s) -> s.startsWith("a"))
        .forEach(System.out::println);
    // "aaa1", "aaa2"
    需要注意的是，排序只创建了一个排列好后的Stream，而不会影响原有的数据源，排序之后原数据stringCollection是不会被修改的
    
    1.1.7.3 Map 映射
    stringCollection
        .stream()
        .map(String::toUpperCase)
        .sorted((a, b) -> b.compareTo(a))
        .forEach(System.out::println);
    // "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
    
    1.1.7.4 Match 匹配
    boolean anyStartsWithA = 
        stringCollection
            .stream()
            .anyMatch((s) -> s.startsWith("a"));
    System.out.println(anyStartsWithA);      // true
    
    boolean allStartsWithA = 
        stringCollection
            .stream()
            .allMatch((s) -> s.startsWith("a"));
    
    System.out.println(allStartsWithA);      // false
    
    boolean noneStartsWithZ = 
        stringCollection
            .stream()
            .noneMatch((s) -> s.startsWith("z"));
    
    System.out.println(noneStartsWithZ);      // true
    
    1.1.7.5 Count 计数
    long startsWithB = 
        stringCollection
            .stream()
            .filter((s) -> s.startsWith("b"))
            .count();
    System.out.println(startsWithB);    // 3
    
    1.1.7.6 Reduce 规约
    Optional<String> reduced =
        stringCollection
            .stream()
            .sorted()
            .reduce((s1, s2) -> s1 + "#" + s2);
    reduced.ifPresent(System.out::println);
    // "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
    
     

2.lambda 表达式
    
    2.1 普通写法 orderiary
    List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
    Collections.sort(names, new Comparator<String>() {
        @Override
        public int compare(String a, String b) {
            return b.compareTo(a);
        }
    });

    2.2 simple 
    Collections.sort(names, (String a, String b) -> {
        return b.compareTo(a);
    });
    
    2.3 most simple
    Collections.sort(names, (String a, String b) -> b.compareTo(a));
    
    Collections.sort(names, (a, b) -> b.compareTo(a));
    
3. 函数式接口
我们可以将lambda表达式当作任意只包含一个抽象方法的接口类型，确保你的接口一定达到这个要求，
你只需要给你的接口添加 @FunctionalInterface 注解，编译器如果发现你标注了这个注解的接口有多于一个抽象方法的时候会报错的
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}
Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);    // 123

4. 方法与构造函数引用
    
    4.1 
    Converter<String, Integer> converter = Integer::valueOf;
    Integer converted = converter.convert("123");
    System.out.println(converted);   // 123
    
    4.2Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用，上面的代码展示了如何引用一个静态方法，我们也可以引用一个对象的方法：
    converter = something::startsWith;
    String converted = converter.convert("Java");
    System.out.println(converted);    // "J"
    
    
    
    4.3 接下来看看构造函数是如何使用::关键字来引用的，首先我们定义一个包含多个构造函数的简单类：
    class Person {
        String firstName;
        String lastName;
        Person() {}
    
        Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
    
    4.3.1 接下来我们指定一个用来创建Person对象的对象工厂接口：
    interface PersonFactory<P extends Person> {
        P create(String firstName, String lastName);
    }
    
    4.3.2 这里我们使用构造函数引用来将他们关联起来，而不是实现一个完整的工厂
    PersonFactory<Person> personFactory = Person::new;
    Person person = personFactory.create("Peter", "Parker");
    
    我们只需要使用 Person::new 来获取Person类构造函数的引用，Java编译器会自动根据PersonFactory.create方法的签名来选择合适的构造函数。
    
5. lambda的作用域
    在lambda表达式中访问外层作用域和老版本的匿名对象中的方式很相似。你可以直接访问标记了final的外层局部变量，或者实例的字段以及静态变量。

6.
