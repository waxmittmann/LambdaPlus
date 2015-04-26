package util.lambdaplus.examples;

import util.lambdaplus.lambda.experimental.GuardedFunction;

import java.util.Optional;
import java.util.function.Function;

public class PersonProcessor {
    public static enum Education {
        NONE, SCHOOL, HIGH_SCHOOL, UNIVERSITY, DOCTORATE
    }

    public static enum ExperienceLevel {
        NONE, GREENHORN, ADEPT, EXPERT, MASTER
    }

    public static class Person {

        private String name;
        private int age;
        private Object experience;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Object getExperience() {
            return experience;
        }
    }

    public static class Employee {

    }

    public Optional<Employee> transformToEmployee(Person somePerson) {
        Function<Boolean, Boolean> stopWhenFalse = (Boolean bool) -> bool;

        GuardedFunction<Person, Boolean> guardedFunc
                = new GuardedFunction((Person p) -> p.getName().equals("Joseph"), stopWhenFalse);
        guardedFunc = guardedFunc.andThen((Person p) -> p.getAge() < 30);
        guardedFunc = guardedFunc.andThen((Person p) -> p.getExperience().isAtLeast(Education.HIGH_SCHOOL));

//        GuardedFunction<Person, Boolean> guardedFunc =
//                new GuardedFunction((Person p) -> p.getName() == "Joseph", stopWhenFalse)
//                        .andThen((Person p) -> p.getAge() < 30)
//                        .andThen((Person p) -> p.getExperience().isAtLeast(Education.HIGH_SCHOOL));

        return guardedFunc.apply(somePerson).map((Boolean isOK) -> {
            if (isOK) {
                return Optional.of(process(somePerson));
            } else {
                return Optional.empty();
            }
        });
    }

    private Employee process(Person person) {
        return null;
    }
}
