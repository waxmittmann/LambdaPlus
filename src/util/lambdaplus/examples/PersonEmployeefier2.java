package util.lambdaplus.examples;

import util.lambdaplus.lambda.experimental.GuardedFunction2;
import util.lambdaplus.lambda.util.general.Tuple2;

public class PersonEmployeefier2 {

    public static class Employee {

        public boolean isEmployable() {
            return false;
        }
    }

    public static class Person {

    }

    public static void main (String [] args) {
        GuardedFunction2<Person, Tuple2<Person, Employee>> guardedFunc2 = new GuardedFunction2<>(
            (Person person) -> new Tuple2(person, transferEducation(person, new Employee())),
            (Person __) -> true,
            (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable()
        );
        guardedFunc2
            .andThen((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
                    transferExperience(in.getFirst(), in.getSecond())))
            .andThen((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable());

//            .andThen(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                    transferName(in.getFirst(), in.getSecond())),
//                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//            .getSecond();



//                .andThen(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                        transferName(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .getSecond();


//        Employee newEmployee =
//            new GuardedFunction2<Person, Tuple2<Person, Employee>>(
//                    (Person person) -> new Tuple2(person, transferEducation(person, new Employee())),
//                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen((Tuple2<Person, Employee> in)-> new Tuple2(in.getFirst(), transferExperience(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                        transferName(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .getSecond();

    }

    private static Employee transferExperience(Person person, Employee employee) {
        return null;
    }

    private static Employee transferName(Person person, Employee employee) {
        return null;
    }

    private static Employee transferAge(Person person, Employee employee) {
        return null;
    }

    private static Employee transferEducation(Person person, Employee employee) {
        return null;
    }
}
